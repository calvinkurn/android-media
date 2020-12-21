package com.tokopedia.play.broadcaster.view.fragment.edit

import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.DialogFragment
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
import com.tokopedia.play.broadcaster.data.datastore.PlayBroadcastSetupDataStore
import com.tokopedia.play.broadcaster.data.type.OverwriteMode
import com.tokopedia.play.broadcaster.di.provider.PlayBroadcastComponentProvider
import com.tokopedia.play.broadcaster.di.setup.DaggerPlayBroadcastSetupComponent
import com.tokopedia.play.broadcaster.util.bottomsheet.PlayBroadcastDialogCustomizer
import com.tokopedia.play.broadcaster.util.model.BreadcrumbsModel
import com.tokopedia.play.broadcaster.view.contract.PlayBottomSheetCoordinator
import com.tokopedia.play.broadcaster.view.contract.ProductSetupListener
import com.tokopedia.play.broadcaster.view.contract.SetupResultListener
import com.tokopedia.play.broadcaster.view.fragment.PlayEtalaseDetailFragment
import com.tokopedia.play.broadcaster.view.fragment.PlayEtalasePickerFragment
import com.tokopedia.play.broadcaster.view.fragment.base.PlayBaseSetupFragment
import com.tokopedia.play.broadcaster.view.viewmodel.DataStoreViewModel
import com.tokopedia.play.broadcaster.view.viewmodel.PlayBroadcastViewModel
import com.tokopedia.play_common.util.extension.cleanBackstack
import com.tokopedia.play_common.util.extension.compatTransitionName
import java.util.*
import javax.inject.Inject

/**
 * Created by jegul on 23/06/20
 */
class ProductSetupBottomSheet : BottomSheetDialogFragment(),
        PlayBottomSheetCoordinator,
        PlayEtalasePickerFragment.Listener,
        ProductSetupListener {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var fragmentFactory: FragmentFactory

    @Inject
    lateinit var dialogCustomizer: PlayBroadcastDialogCustomizer

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>

    private lateinit var flFragment: FrameLayout
    private lateinit var flOverlay: FrameLayout

    private lateinit var parentViewModel: PlayBroadcastViewModel
    private lateinit var dataStoreViewModel: DataStoreViewModel

    private var mListener: SetupResultListener? = null

    private val currentFragment: Fragment?
        get() = childFragmentManager.findFragmentById(R.id.fl_fragment)

    private val fragmentBreadcrumbs = Stack<BreadcrumbsModel>()

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
        }.apply {
            dialogCustomizer.customize(this)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        inject()
        childFragmentManager.fragmentFactory = fragmentFactory
        super.onCreate(savedInstanceState)
        cleanBackstack()
        setStyle(DialogFragment.STYLE_NORMAL, R.style.BottomSheet_Setup_Pinned)
        parentViewModel = ViewModelProviders.of(requireActivity(), viewModelFactory).get(PlayBroadcastViewModel::class.java)
        dataStoreViewModel = ViewModelProviders.of(this, viewModelFactory).get(DataStoreViewModel::class.java)
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

    override fun goBack() {
        dialog?.onBackPressed()
    }

    override fun <T : Fragment> navigateToFragment(fragmentClass: Class<out T>, extras: Bundle, sharedElements: List<View>, onFragment: (T) -> Unit) {
        addBreadcrumb()
        openFragment(fragmentClass, extras, sharedElements, onFragment)
    }

    override fun onEtalaseClicked(id: String, sharedElements: List<View>) {
        navigateToFragment(
                fragmentClass = PlayEtalaseDetailFragment::class.java,
                sharedElements = sharedElements,
                extras = Bundle().apply {
                    putString(PlayEtalaseDetailFragment.EXTRA_ETALASE_ID, id)
                }
        )
    }

    override suspend fun onProductSetupFinished(sharedElements: List<View>, dataStore: PlayBroadcastSetupDataStore): Throwable? {
        val error = mListener?.onSetupCompletedWithData(this, dataStore)
        return if (error == null) {
            dismiss()
            null
        } else error
    }

    override fun onAttachFragment(childFragment: Fragment) {
        super.onAttachFragment(childFragment)

        when (childFragment) {
            is PlayEtalasePickerFragment -> childFragment.setListener(this)
            is PlayEtalaseDetailFragment -> childFragment.setListener(this)
        }
    }

    fun setListener(listener: SetupResultListener) {
        mListener = listener
    }

    fun show(fragmentManager: FragmentManager) {
        show(fragmentManager, TAG)
    }

    private fun inject() {
        DaggerPlayBroadcastSetupComponent.builder()
                .setBroadcastComponent((requireActivity() as PlayBroadcastComponentProvider).getBroadcastComponent())
                .build()
                .inject(this)
    }

    private fun initView(view: View) {
        with(view) {
            flFragment = findViewById(R.id.fl_fragment)
            flOverlay = findViewById(R.id.fl_overlay)
        }
    }

    private fun setupView(view: View) {
        dataStoreViewModel.setDataStore(parentViewModel.getCurrentSetupDataStore(), modeExclusion = listOf(OverwriteMode.Product))

        flOverlay.setOnClickListener { dialog?.onBackPressed() }

        openEtalasePickerFragment()
    }

    private fun openEtalasePickerFragment() {
        navigateToFragment(
                fragmentClass = PlayEtalasePickerFragment::class.java
        )
    }

    private fun<T: Fragment> openFragment(fragmentClass: Class<out T>, extras: Bundle, sharedElements: List<View>, onFragment: (T) -> Unit): Fragment {
        val fragmentTransaction = childFragmentManager.beginTransaction()
        val destFragment = getFragmentByClassName(fragmentClass)
        destFragment.arguments = extras
        onFragment(destFragment as T)
        fragmentTransaction
                .apply {
                    sharedElements.forEach {
                        val transitionName = it.compatTransitionName
                        if (transitionName != null) addSharedElement(it, transitionName)
                    }

                    if (sharedElements.isNotEmpty()) setReorderingAllowed(true)
                }
                .replace(flFragment.id, destFragment, fragmentClass.name)
                .addToBackStack(fragmentClass.name)
                .commit()

        return destFragment
    }

    private fun getFragmentByClassName(fragmentClass: Class<out Fragment>): Fragment {
        return childFragmentManager.fragmentFactory.instantiate(requireContext().classLoader, fragmentClass.name)
    }

    private fun addBreadcrumb() {
        currentFragment?.let { fragment ->
            fragmentBreadcrumbs.add(
                    BreadcrumbsModel(fragment.javaClass, fragment.arguments
                            ?: Bundle.EMPTY)
            )
        }
    }

    private fun setupDialog(dialog: Dialog) {
        dialog.setOnShowListener {
            val bottomSheetDialog = dialog as BottomSheetDialog
            val bottomSheet = bottomSheetDialog.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.layoutParams = bottomSheet?.layoutParams?.apply {
                height = ViewGroup.LayoutParams.MATCH_PARENT
            }
            bottomSheet?.setBackgroundColor(Color.TRANSPARENT)
            bottomSheet?.let {
                bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
                bottomSheetBehavior.isHideable = false
                bottomSheetBehavior.peekHeight = maxHeight()
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            }

            isCancelable = false
        }
    }

    private fun maxHeight(): Int = getScreenHeight()

    companion object {

        private const val TAG = "Product Setup BottomSheet"
    }
}