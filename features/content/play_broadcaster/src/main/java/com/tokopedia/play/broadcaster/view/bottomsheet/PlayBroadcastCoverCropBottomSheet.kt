package com.tokopedia.play.broadcaster.view.bottomsheet

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.ui.model.CoverSourceEnum
import com.tokopedia.play.broadcaster.ui.model.CoverStarterEnum
import com.tokopedia.play.broadcaster.view.contract.PlayBottomSheetCoordinator
import com.tokopedia.play.broadcaster.view.fragment.PlayCoverTitleSetupFragment
import com.tokopedia.play.broadcaster.view.fragment.PlayCoverTitleSetupFragment.Companion.EXTRA_COVER_SOURCE
import com.tokopedia.play.broadcaster.view.fragment.PlayCoverTitleSetupFragment.Companion.EXTRA_COVER_URI
import com.tokopedia.play.broadcaster.view.fragment.PlayCoverTitleSetupFragment.Companion.EXTRA_SELECTED_PRODUCT_IMAGE_URL_LIST
import com.tokopedia.play.broadcaster.view.fragment.PlayCoverTitleSetupFragment.Companion.EXTRA_STARTER_STATE
import com.tokopedia.unifycomponents.BottomSheetUnify
import javax.inject.Inject

/**
 * @author by furqan on 12/06/2020
 */
class PlayBroadcastCoverCropBottomSheet @Inject constructor(
        private val viewModelFactory: ViewModelFactory)
    : BottomSheetUnify(), PlayCoverTitleSetupFragment.ListenerForCropOnly,
        PlayBottomSheetCoordinator {

    var listener: Listener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBottomSheet()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    override fun onFinishedCrop(imageUri: Uri, imageUrl: String) {
        listener?.onCoverEdited(imageUri, imageUrl)
        dismiss()
    }

    override fun onCancelCrop(coverSource: CoverSourceEnum) {
        listener?.onCancelCrop(coverSource)
        dismiss()
    }

    override fun saveCoverAndTitle(coverUri: Uri, coverUrl: String, liveTitle: String) {
        // do nothing
    }

    override fun showBottomAction(shouldShow: Boolean) {
        // do nothing
    }

    override fun navigateToFragment(fragmentClass: Class<out Fragment>, extras: Bundle, sharedElements: List<View>, onFragment: (Fragment) -> Unit) {
        // do nothing
    }

    override fun setupTitle(title: String) {
        setTitle(title)
    }

    private fun initBottomSheet() {
        isDragable = true
        isHideable = true
        setTitle(getString(R.string.play_prepare_cover_title_add_cover_label))
        setChild(View.inflate(requireContext(), R.layout.bottom_sheet_play_broadcast_cover_crop, null))
    }

    private fun initView() {
        bottomSheetHeader.setPadding(
                resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.layout_lvl2),
                bottomSheetHeader.top,
                bottomSheetWrapper.right,
                bottomSheetHeader.bottom)
        bottomSheetWrapper.setPadding(0,
                bottomSheetWrapper.paddingTop,
                0,
                bottomSheetWrapper.paddingBottom)

        setupCoverCropFragment()
    }

    private fun setupCoverCropFragment() {
        val coverCropArguments = arguments?.let {
            Bundle().apply {
                putInt(EXTRA_STARTER_STATE, it.getInt(EXTRA_STARTER_STATE, CoverStarterEnum.NORMAL.value))
                putInt(EXTRA_COVER_SOURCE, it.getInt(EXTRA_COVER_SOURCE, CoverSourceEnum.NONE.value))
                if (it.containsKey(EXTRA_COVER_URI)) {
                    putParcelable(EXTRA_COVER_URI, it.getParcelable<Uri>(EXTRA_COVER_URI))
                } else if (it.containsKey(EXTRA_SELECTED_PRODUCT_IMAGE_URL_LIST)) {
                    putSerializable(EXTRA_SELECTED_PRODUCT_IMAGE_URL_LIST, it.getSerializable(EXTRA_SELECTED_PRODUCT_IMAGE_URL_LIST))
                }
            }
        }
        val targetFragment = getCoverTitleFragment()
        targetFragment.arguments = coverCropArguments
        targetFragment.listenerForCropOnly = this
        val fragmentTransaction = childFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.flFragment, targetFragment,
                PlayCoverTitleSetupFragment::class.java.name)
                .commit()
    }

    private fun getCoverTitleFragment(): PlayCoverTitleSetupFragment =
            childFragmentManager.fragmentFactory.instantiate(
                    PlayCoverTitleSetupFragment::class.java.classLoader!!,
                    PlayCoverTitleSetupFragment::class.java.name) as PlayCoverTitleSetupFragment

    interface Listener {
        fun onCoverEdited(imageUri: Uri, imageUrl: String)
        fun onCancelCrop(coverSource: CoverSourceEnum)
    }

    companion object {
        const val TAG = "TagPlayBroadcastCoverCropBottomSheet"
    }

}