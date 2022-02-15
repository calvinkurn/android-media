package com.tokopedia.play.broadcaster.view.bottomsheet

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
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.kotlin.extensions.view.getScreenHeight
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.data.datastore.PlayBroadcastSetupDataStore
import com.tokopedia.play.broadcaster.di.setup.DaggerPlayBroadcastSetupComponent
import com.tokopedia.play.broadcaster.util.bottomsheet.PlayBroadcastDialogCustomizer
import com.tokopedia.play.broadcaster.view.contract.PlayBottomSheetCoordinator
import com.tokopedia.play.broadcaster.view.contract.SetupResultListener
import com.tokopedia.play.broadcaster.view.fragment.setup.cover.PlayCoverSetupFragment
import com.tokopedia.play.broadcaster.view.fragment.base.PlayBaseSetupFragment
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.play.broadcaster.di.DaggerActivityRetainedComponent
import com.tokopedia.play.broadcaster.ui.model.PlayCoverUiModel
import com.tokopedia.play.broadcaster.ui.model.product.ProductUiModel
import com.tokopedia.play.broadcaster.util.delegate.retainedComponent
import com.tokopedia.play.broadcaster.util.pageflow.FragmentPageNavigator
import com.tokopedia.play_common.lifecycle.lifecycleBound
import com.tokopedia.play_common.util.extension.cleanBackstack
import javax.inject.Inject

/**
 * Created by jegul on 26/05/20
 */
class PlayBroadcastSetupBottomSheet :
        BottomSheetDialogFragment(),
        PlayBottomSheetCoordinator,
        PlayCoverSetupFragment.Listener
{

    private val retainedComponent by retainedComponent({ requireActivity() }) {
        DaggerActivityRetainedComponent.builder()
            .baseAppComponent(
                (requireActivity().application as BaseMainApplication).baseAppComponent
            )
            .build()
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var fragmentFactory: FragmentFactory

    @Inject
    lateinit var dispatcher: CoroutineDispatchers

    @Inject
    lateinit var dialogCustomizer: PlayBroadcastDialogCustomizer

    private lateinit var flFragment: FrameLayout
    private lateinit var flOverlay: FrameLayout

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>

    private var mListener: Listener? = null
    private var mDataSource: DataSource? = null

    private val currentFragment: Fragment?
        get() = childFragmentManager.findFragmentById(R.id.fl_fragment)

    private val pageNavigator: FragmentPageNavigator by lifecycleBound(
            creator = {
                FragmentPageNavigator(
                        fragmentManager = childFragmentManager
                )
            }
    )

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return object : BottomSheetDialog(requireContext(), theme) {
            override fun onBackPressed() {
                val currentFragment = childFragmentManager.findFragmentById(R.id.fl_fragment)
                if (currentFragment is PlayBaseSetupFragment && currentFragment.onInterceptBackPressed()) return

                if (childFragmentManager.backStackEntryCount > 1) {
                    childFragmentManager.popBackStack()
                } else {
                    cancel()
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

    override fun <T : Fragment> navigateToFragment(fragmentClass: Class<out T>, extras: Bundle, sharedElements: List<View>, onFragment: (T) -> Unit) {
        openFragment(fragmentClass, extras, sharedElements)
    }

    override fun goBack() {
        dialog?.onBackPressed()
    }

    override fun onCoverSetupFinished(cover: PlayCoverUiModel) {
        mListener?.onCoverChanged(cover)
        dismiss()
    }

    override fun onAttachFragment(childFragment: Fragment) {
        super.onAttachFragment(childFragment)

        when (childFragment) {
            is PlayCoverSetupFragment -> {
                childFragment.setListener(this)
                childFragment.setDataSource(object : PlayCoverSetupFragment.DataSource {
                    override fun getProductList(): List<ProductUiModel> {
                        return mDataSource?.getProductList().orEmpty()
                    }
                })
            }
        }
    }

    fun show(fragmentManager: FragmentManager) {
        show(fragmentManager, TAG)
    }

    fun setDataSource(dataSource: DataSource?) {
        mDataSource = dataSource
    }

    fun setListener(listener: Listener?) {
        mListener = listener
    }

    private fun inject() {
        DaggerPlayBroadcastSetupComponent.builder()
                .setActivityRetainedComponent(retainedComponent)
                .build()
                .inject(this)
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

    private fun initView(view: View) {
        with(view) {
            flFragment = findViewById(R.id.fl_fragment)
            flOverlay = findViewById(R.id.fl_overlay)
        }
    }

    private fun setupView(view: View) {
        flOverlay.setOnClickListener { dialog?.onBackPressed() }

        navigateToFragment(
                PlayCoverSetupFragment::class.java
        )
    }

    private fun maxHeight(): Int = getScreenHeight()

    private fun<T: Fragment> openFragment(
            fragmentClass: Class<out T>,
            extras: Bundle,
            sharedElements: List<View>,
    ) {
        pageNavigator.navigate(
                flFragment.id,
                fragmentClass,
                extras,
                sharedElements
        )
    }

    companion object {
        private const val TAG = "PlayBroadcastSetupBottomSheet"
    }

    interface DataSource {
        fun getProductList(): List<ProductUiModel>
    }

    interface Listener {
        fun onCoverChanged(cover: PlayCoverUiModel)
    }
}