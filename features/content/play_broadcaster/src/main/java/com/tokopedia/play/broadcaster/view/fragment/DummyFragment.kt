package com.tokopedia.play.broadcaster.view.fragment

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.ui.model.CoverSourceEnum
import com.tokopedia.play.broadcaster.ui.model.CoverStarterEnum
import com.tokopedia.play.broadcaster.view.bottomsheet.PlayBroadcastChooseCoverBottomSheet
import com.tokopedia.play.broadcaster.view.bottomsheet.PlayBroadcastCoverCropBottomSheet
import com.tokopedia.play.broadcaster.view.bottomsheet.PlayBroadcastCoverFromGalleryBottomSheet
import com.tokopedia.play.broadcaster.view.bottomsheet.PlayBroadcastEditTitleBottomSheet
import com.tokopedia.play.broadcaster.view.fragment.base.PlayBaseBroadcastFragment
import kotlinx.android.synthetic.main.layout_play_cover_title_setup.*
import javax.inject.Inject

/**
 * @author by furqan on 12/06/2020
 */
class DummyFragment @Inject constructor()
    : PlayBaseBroadcastFragment(),
        /** Please copy these implementation */
        PlayBroadcastEditTitleBottomSheet.Listener,
        PlayBroadcastChooseCoverBottomSheet.Listener,
        PlayBroadcastCoverCropBottomSheet.Listener,
        PlayBroadcastCoverFromGalleryBottomSheet.Listener {

    private val dummyPairOfProductIdAndImageUrlList =
            arrayListOf<Pair<Long, String>>(
                    Pair(391452384, "https://ecs7.tokopedia.net/img/cache/300/product-1/2019/1/17/2163625/2163625_0d01ab57-d40f-4543-a892-7434116ef34a_747_747.jpg")
            )

    override fun getScreenName(): String = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
            inflater.inflate(R.layout.layout_play_cover_title_setup, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        etPlayCoverTitleText.setOnClickListener {
            showEditTitleBottomSheet("Dummy Title")
        }
        containerChangeCover.setOnClickListener {
            showEditCoverBottomSheet(dummyPairOfProductIdAndImageUrlList)
        }
    }

    /**
     * This is method from Edit Text Bottom Sheet Listener,
     * you can use this method to get the edited title
     */
    override fun onSaveEditedTitle(title: String) {
        etPlayCoverTitleText.setText(title)
    }

    /**
     * PLEASE COPY THIS
     *
     * Please copy the implementation of this method
     *
     * This is method from Edit Cover Bottom Sheet Listener,
     * this method will be called when user take cover from camera
     * we will show crop fragment when this method is called
     */
    override fun onGetCoverFromCamera(imageUri: Uri?) {
        navigateToCropLayout(CoverSourceEnum.CAMERA, imageUri)
    }

    /**
     * PLEASE COPY THIS
     *
     * Please copy the implementation of this method
     *
     * This is method from Edit Cover Bottom Sheet Listener,
     * this method will be called when user take cover from product image
     * we will show crop fragment when this method is called
     */
    override fun onGetCoverFromProduct(productPosition: Int) {
        navigateToCropLayout(CoverSourceEnum.PRODUCT,
                coverSelectedDetail = dummyPairOfProductIdAndImageUrlList[productPosition])
    }

    /**
     * PLEASE COPY THIS
     *
     * Please copy the implementation of this method
     *
     * This is method from Edit Cover Bottom Sheet Listener,
     * this method will be called when user take cover from gallery
     * we will show select from gallery bottom sheet
     */
    override fun onChooseFromGalleryClicked() {
        val coverFromGalleryBottomSheet = PlayBroadcastCoverFromGalleryBottomSheet()
        coverFromGalleryBottomSheet.listener = this
        coverFromGalleryBottomSheet.setShowListener { coverFromGalleryBottomSheet.bottomSheet.state = BottomSheetBehavior.STATE_EXPANDED }
        coverFromGalleryBottomSheet.show(requireFragmentManager(), PlayBroadcastChooseCoverBottomSheet.TAG_CHOOSE_COVER)
    }

    /**
     * PLEASE COPY THIS
     *
     * Please copy the implementation of this method
     *
     * This is method from Play Cover From Gallery Bottom Sheet Listener,
     * this method will be called when user take cover from gallery
     * we will show crop fragment when this method is called
     */
    override fun onGetCoverFromGallery(imageUri: Uri?) {
        imageUri?.let {
            navigateToCropLayout(CoverSourceEnum.GALLERY, coverImageUri = it)
        }
    }

    /**
     * This is method from Edit Cover Bottom Sheet Listener,
     * you can use this method to get the edited cover
     */
    override fun onCoverEdited(imageUri: Uri, imageUrl: String) {
        ivPlayCoverImage.setImageURI(imageUri)
    }

    /**
     * PLEASE COPY THIS
     *
     * This is method from Edit Cover Bottom Sheet Listener,
     * you can use this method to do something when user click "Ganti" on Crop Page
     */
    override fun onCancelCrop(coverSource: CoverSourceEnum) {
        when (coverSource) {
            CoverSourceEnum.GALLERY -> {
                onChooseFromGalleryClicked()
            }
            else -> {
                showEditCoverBottomSheet(dummyPairOfProductIdAndImageUrlList)
            }
        }
    }

    /**
     * PLEASE COPY THIS
     *
     * Please copy this method
     *
     * This method is used to open Edit Title Bottom Sheet
     */
    private fun showEditTitleBottomSheet(currentTitle: String) {
        val editTitleBottomSheet = PlayBroadcastEditTitleBottomSheet.getInstance(currentTitle)
        editTitleBottomSheet.listener = this
        editTitleBottomSheet.setShowListener { editTitleBottomSheet.bottomSheet.state = BottomSheetBehavior.STATE_EXPANDED }
        editTitleBottomSheet.show(requireFragmentManager(), PlayBroadcastEditTitleBottomSheet.TAG)
    }

    /**
     * PLEASE COPY THIS
     *
     * Please copy this method
     *
     * This method is used to open Edit Cover Bottom Sheet
     */
    private fun showEditCoverBottomSheet(pairOfProductIdAndImageUrlList: ArrayList<Pair<Long, String>>) {
        val editCoverBottomSheet = PlayBroadcastChooseCoverBottomSheet.getInstance(pairOfProductIdAndImageUrlList)
        editCoverBottomSheet.listener = this
        editCoverBottomSheet.setShowListener { editCoverBottomSheet.bottomSheet.state = BottomSheetBehavior.STATE_EXPANDED }
        editCoverBottomSheet.show(requireFragmentManager(), PlayBroadcastEditTitleBottomSheet.TAG)
    }

    /**
     * PLEASE COPY THIS
     *
     * Please copy this method
     *
     * This method is used to navigate to Crop Layout to Crop Cover
     */
    private fun navigateToCropLayout(coverSource: CoverSourceEnum,
                                     coverImageUri: Uri? = null,
                                     coverSelectedDetail: Pair<Long, String>? = null) {
        val fragment = childFragmentManager.fragmentFactory.instantiate(
                requireContext().classLoader,
                PlayBroadcastCoverCropBottomSheet::class.java.name) as PlayBroadcastCoverCropBottomSheet
        fragment.arguments = Bundle().apply {
            putInt(PlayCoverTitleSetupFragment.EXTRA_STARTER_STATE, CoverStarterEnum.CROP_ONLY.value)
            putInt(PlayCoverTitleSetupFragment.EXTRA_COVER_SOURCE, coverSource.value)
            coverImageUri?.let {
                putParcelable(PlayCoverTitleSetupFragment.EXTRA_COVER_URI, it)
            }
            coverSelectedDetail?.let {
                putSerializable(PlayCoverTitleSetupFragment.EXTRA_SELECTED_PRODUCT_IMAGE_URL_LIST, arrayListOf(it))
            }
        }
        fragment.listener = this
        fragment.setShowListener { fragment.bottomSheet.state = BottomSheetBehavior.STATE_EXPANDED }
        fragment.show(requireFragmentManager(), PlayBroadcastCoverCropBottomSheet.TAG)
    }
}