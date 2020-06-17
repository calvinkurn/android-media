package com.tokopedia.play.broadcaster.view.bottomsheet

import android.app.Dialog
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.kotlin.extensions.view.getScreenHeight
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.ui.model.PlayCoverUiModel
import com.tokopedia.play.broadcaster.ui.model.ProductContentUiModel
import com.tokopedia.play.broadcaster.util.BreadcrumbsModel
import com.tokopedia.play.broadcaster.util.compatTransitionName
import com.tokopedia.play.broadcaster.view.contract.PlayBottomSheetCoordinator
import com.tokopedia.play.broadcaster.view.fragment.PlayEtalasePickerFragment
import com.tokopedia.play.broadcaster.view.fragment.base.PlayBaseSetupFragment
import com.tokopedia.play.broadcaster.view.viewmodel.PlayBroadcastSetupViewModel
import com.tokopedia.play.broadcaster.view.viewmodel.PlayEtalasePickerViewModel
import java.util.*
import javax.inject.Inject

/**
 * Created by jegul on 26/05/20
 */
class PlayBroadcastSetupBottomSheet @Inject constructor(
        private val viewModelFactory: ViewModelFactory
) : BottomSheetDialogFragment(), PlayBottomSheetCoordinator {

    private lateinit var parentViewModel: PlayBroadcastSetupViewModel
    private lateinit var viewModel: PlayEtalasePickerViewModel

    private lateinit var flFragment: FrameLayout
    private lateinit var flOverlay: FrameLayout

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>

    private val fragmentBreadcrumbs = Stack<BreadcrumbsModel>()

    private var mListener: Listener? = null

    private val currentFragment: Fragment?
        get() = childFragmentManager.findFragmentById(R.id.fl_fragment)

    private val fragmentFactory: FragmentFactory
        get() = childFragmentManager.fragmentFactory

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return object : BottomSheetDialog(requireContext(), theme) {
            override fun onBackPressed() {
                val currentFragment = childFragmentManager.findFragmentById(R.id.fl_fragment)
                if (currentFragment is PlayBaseSetupFragment && currentFragment.onInterceptBackPressed()) return

                if (!fragmentBreadcrumbs.empty()) {
                    val lastFragmentBreadcrumbs = fragmentBreadcrumbs.pop()
                    childFragmentManager.popBackStack(lastFragmentBreadcrumbs.fragmentClass.name, 0)
                } else {
                    cancel()
                    mListener?.onSetupCanceled()
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setStyle(DialogFragment.STYLE_NORMAL, R.style.Style_FloatingBottomSheet)
        parentViewModel = ViewModelProviders.of(requireActivity(), viewModelFactory).get(PlayBroadcastSetupViewModel::class.java)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(PlayEtalasePickerViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.bottom_sheet_play_broadcast_setup, container, false)
        dialog?.let { setupDialog(it) }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
        setupView(view)
    }

    override fun navigateToFragment(fragmentClass: Class<out Fragment>, extras: Bundle, sharedElements: List<View>, onFragment: (Fragment) -> Unit) {
        addBreadcrumb()
        openFragment(fragmentClass, extras, sharedElements, onFragment)
    }

    override fun saveCoverAndTitle(coverUri: Uri, coverUrl: String, liveTitle: String) {
        viewModel.coverImageUri = coverUri
        viewModel.coverImageUrl = coverUrl
        viewModel.liveTitle = liveTitle
        complete()
    }

    override fun goBack() {
        dialog?.onBackPressed()
    }

    fun show(fragmentManager: FragmentManager) {
        show(fragmentManager, TAG)
    }

    fun complete() {
        dismiss()
        mListener?.onSetupCompletedWithData(
                selectedProducts = viewModel.selectedProductList,
                cover = PlayCoverUiModel(
                        coverImageUri = viewModel.coverImageUri,
                        coverImageUrl = viewModel.coverImageUrl,
                        liveTitle = viewModel.liveTitle
                )
        )
    }

    fun setListener(listener: Listener) {
        mListener = listener
    }

    private fun setupDialog(dialog: Dialog) {
        dialog.setOnShowListener {
            val bottomSheetDialog = dialog as BottomSheetDialog
            val bottomSheet = bottomSheetDialog.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.layoutParams = bottomSheet?.layoutParams?.apply {
                height = ViewGroup.LayoutParams.MATCH_PARENT
            }
            bottomSheet?.setBackgroundColor(Color.TRANSPARENT)
            bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
            bottomSheetBehavior.isHideable = false
            bottomSheetBehavior.peekHeight = maxHeight()
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED

            isCancelable = false
        }
    }

    private fun initView(view: View) {
        with(view) {
            flFragment = findViewById(R.id.fl_fragment)
            flOverlay = findViewById(R.id.fl_overlay)
        }
    }

    private fun setupView(view: View) {
        flOverlay.setOnClickListener { dialog?.onBackPressed() }

        navigateToFragment(PlayEtalasePickerFragment::class.java)
    }

    private fun maxHeight(): Int = getScreenHeight()

    private fun openFragment(fragmentClass: Class<out Fragment>, extras: Bundle, sharedElements: List<View>, onFragment: (Fragment) -> Unit): Fragment {
        val fragmentTransaction = childFragmentManager.beginTransaction()
        val destFragment = getFragmentByClassName(fragmentClass)
        destFragment.arguments = extras
        onFragment(destFragment)
        fragmentTransaction
                .apply {
                    sharedElements.forEach {
                        val transitionName = it.compatTransitionName
                        if (transitionName != null) addSharedElement(it, transitionName)
                    }

                    if (sharedElements.isNotEmpty()) setReorderingAllowed(true)
                }
                .replace(R.id.fl_fragment, destFragment, fragmentClass.name)
                .addToBackStack(fragmentClass.name)
                .commit()

        return destFragment
    }

    private fun getFragmentByClassName(fragmentClass: Class<out Fragment>): Fragment {
        return fragmentFactory.instantiate(fragmentClass.classLoader!!, fragmentClass.name)
    }

    private fun addBreadcrumb() {
        currentFragment?.let { fragment ->
            fragmentBreadcrumbs.add(
                    BreadcrumbsModel(fragment.javaClass, fragment.arguments ?: Bundle.EMPTY)
            )
        }
    }

    private fun saveCompleteChannel() {
        parentViewModel.saveCompleteChannel(
                productList = viewModel.selectedProductList,
                coverUrl = viewModel.coverImageUrl,
                coverUri = viewModel.coverImageUri,
                title = viewModel.liveTitle
        )
    }

    /**
     * Want to test "Ubah Promo"? you only need to send percentage and quota params when `getInstance()`
     * Want to test "Enter from Left"? you only need to send isBack param as true when `getInstance()`
     * (animation that used when user back to create promo bottomsheets after
     * navigate to choose live cover bottomsheets)
     */
    private fun navigateToVoucherCreationBottomSheet(isBack: Boolean = false) {
        val voucherCreationBottomSheet = PlayPrepareBroadcastCreatePromoBottomSheet.getInstance(isBack = isBack)
        voucherCreationBottomSheet.listener = object : PlayPrepareBroadcastCreatePromoBottomSheet.Listener {
            override fun onVoucherSaved(isWithPromo: Boolean, promoPercentage: Int, promoQuota: Int) {
                // set promo detail to parent model
                // navigate to next page
            }
        }
        fragmentManager?.let {
            voucherCreationBottomSheet.show(it, PlayPrepareBroadcastCreatePromoBottomSheet.TAG_CREATE_PROMO_BOTTOM_SHEETS)
        }
    }

    companion object {
        private const val TAG = "PlayBroadcastSetupBottomSheet"
    }

    interface Listener {

        fun onSetupCanceled()
        fun onSetupCompletedWithData(
                selectedProducts: List<ProductContentUiModel>,
                cover: PlayCoverUiModel
        )
    }
}