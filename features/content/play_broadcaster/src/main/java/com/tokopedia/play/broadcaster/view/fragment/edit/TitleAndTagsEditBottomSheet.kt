package com.tokopedia.play.broadcaster.view.fragment.edit

import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.util.bottomsheet.PlayBroadcastDialogCustomizer
import com.tokopedia.play.broadcaster.util.model.BreadcrumbsModel
import com.tokopedia.play.broadcaster.view.contract.PlayBottomSheetCoordinator
import com.tokopedia.play.broadcaster.view.contract.SetupResultListener
import com.tokopedia.play.broadcaster.view.fragment.base.PlayBaseSetupFragment
import com.tokopedia.play.broadcaster.view.viewmodel.DataStoreViewModel
import com.tokopedia.play.broadcaster.view.viewmodel.PlayBroadcastViewModel
import java.util.*
import javax.inject.Inject

/**
 * Created by jegul on 22/04/21
 */
class TitleAndTagsEditBottomSheet : BottomSheetDialogFragment(),
        PlayBottomSheetCoordinator {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var fragmentFactory: FragmentFactory

    @Inject
    lateinit var dialogCustomizer: PlayBroadcastDialogCustomizer

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>

    private lateinit var parentViewModel: PlayBroadcastViewModel
    private lateinit var dataStoreViewModel: DataStoreViewModel

    private var mListener: SetupResultListener? = null

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

    override fun goBack() {
        TODO("Not yet implemented")
    }

    override fun <T : Fragment> navigateToFragment(fragmentClass: Class<out T>, extras: Bundle, sharedElements: List<View>, onFragment: (T) -> Unit) {
        TODO("Not yet implemented")
    }
}