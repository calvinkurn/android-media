package com.tokopedia.play.broadcaster.view.bottomsheet

import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.kotlin.extensions.view.getScreenHeight
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.util.BreadcrumbsModel
import com.tokopedia.play.broadcaster.view.contract.PlayBottomSheetCoordinator
import com.tokopedia.play.broadcaster.view.fragment.PlayEtalasePickerFragment
import com.tokopedia.play.broadcaster.view.fragment.base.PlayBaseSetupFragment
import com.tokopedia.play.broadcaster.view.partial.SelectedProductPagePartialView
import com.tokopedia.play.broadcaster.view.viewmodel.PlayEtalasePickerViewModel
import com.tokopedia.unifycomponents.UnifyButton
import java.util.*
import javax.inject.Inject

/**
 * Created by jegul on 26/05/20
 */
class PlayBroadcastSetupBottomSheet @Inject constructor(
        private val viewModelFactory: ViewModelFactory,
        private val fragmentFactory: FragmentFactory
) : BottomSheetDialogFragment(), PlayBottomSheetCoordinator {

    private lateinit var viewModel: PlayEtalasePickerViewModel

    private lateinit var flFragment: FrameLayout
    private lateinit var ivBack: ImageView
    private lateinit var tvTitle: TextView
    private lateinit var clContent: ConstraintLayout
    private lateinit var flOverlay: FrameLayout
    private lateinit var ivInventory: ImageView
    private lateinit var btnAction: UnifyButton
    private lateinit var tvBadgeCount: TextView

    private lateinit var selectedProductPage: SelectedProductPagePartialView

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>

    private val fragmentBreadcrumbs = Stack<BreadcrumbsModel>()

    private val currentFragment: Fragment?
        get() = childFragmentManager.findFragmentById(R.id.fl_fragment)

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return object : BottomSheetDialog(requireContext(), theme) {
            override fun onBackPressed() {
                if (!fragmentBreadcrumbs.empty()) {
                    val lastFragmentBreadcrumbs = fragmentBreadcrumbs.pop()
                    navigateToFragment(lastFragmentBreadcrumbs.fragmentClass, lastFragmentBreadcrumbs.extras, recordBreadcrumbs = false)
                } else super.onBackPressed()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        childFragmentManager.fragmentFactory = fragmentFactory
        super.onCreate(savedInstanceState)
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

        selectedProductPage = SelectedProductPagePartialView(view as ViewGroup, object : SelectedProductPagePartialView.Listener {
            override fun onProductSelectStateChanged(productId: Long, isSelected: Boolean) {
                viewModel.selectProduct(productId, isSelected)
                val activeFragment = currentFragment
                if (activeFragment is PlayBaseSetupFragment) activeFragment.refresh()
            }
        })
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        observeSelectedProducts()
    }

    override fun navigateToFragment(fragmentClass: Class<out Fragment>, extras: Bundle, recordBreadcrumbs: Boolean) {
        if (recordBreadcrumbs) addBreadcrumb()
        val fragment = openFragment(fragmentClass, extras)

        if (fragment is PlayBaseSetupFragment) {
            setupTitle(fragment.getTitle())
            ivBack.setImageResource(
                    if (fragment.isRootFragment()) com.tokopedia.unifycomponents.R.drawable.unify_bottomsheet_close
                    else R.drawable.ic_system_action_back_grayscale_24
            )
        } else {
            setupTitle("")
            ivBack.setImageResource(com.tokopedia.unifycomponents.R.drawable.unify_bottomsheet_close)
        }
    }

    override fun setupTitle(title: String) {
        tvTitle.text = title
    }

    fun show(fragmentManager: FragmentManager) {
        show(fragmentManager, TAG)
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
            ivBack = findViewById(R.id.iv_back)
            tvTitle = findViewById(R.id.tv_title)
            clContent = findViewById(R.id.cl_content)
            flOverlay = findViewById(R.id.fl_overlay)
            ivInventory = findViewById(R.id.iv_inventory)
            btnAction = findViewById(R.id.btn_action)
            tvBadgeCount = findViewById(R.id.tv_badge_count)
        }
    }

    private fun setupView(view: View) {
        flOverlay.setOnClickListener { dialog?.onBackPressed() }
        ivBack.setOnClickListener { dialog?.onBackPressed() }

        ivInventory.setOnClickListener { showSelectedProductPage() }

        navigateToFragment(PlayEtalasePickerFragment::class.java)
    }

    private fun maxHeight(): Int = (getScreenHeight()).toInt()

    private fun openFragment(fragmentClass: Class<out Fragment>, extras: Bundle): Fragment {
        val fragmentTransaction = childFragmentManager.beginTransaction()
        val destFragment = getFragmentByClassName(fragmentClass)
        destFragment.arguments = extras
        fragmentTransaction
                .replace(R.id.fl_fragment, destFragment, fragmentClass.name)
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

    private fun showSelectedProductPage() {
        if (selectedProductPage.isShown) return

        selectedProductPage.setSelectedProductList(viewModel.selectedProductList)
        selectedProductPage.show()
    }

    //region observe
    /**
     * Observe
     */
    private fun observeSelectedProducts() {
        viewModel.observableSelectedProducts.observe(viewLifecycleOwner, Observer {
            if (it.isEmpty()) {
                ivInventory.setImageResource(R.drawable.ic_play_inventory_disabled)
                ivInventory.isClickable = false
                btnAction.isEnabled = false
                tvBadgeCount.gone()
            } else {
                ivInventory.setImageResource(R.drawable.ic_play_inventory)
                ivInventory.isClickable = true
                btnAction.isEnabled = true
                tvBadgeCount.visible()
                tvBadgeCount.text = it.size.toString()
            }

            selectedProductPage.onSelectedProductsUpdated(it)
        })
    }
    //endregion

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

        private const val MAX_HEIGHT_MULTIPLIER = 0.8
    }
}