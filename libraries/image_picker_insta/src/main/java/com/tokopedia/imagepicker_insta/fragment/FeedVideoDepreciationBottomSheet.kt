package com.tokopedia.imagepicker_insta.fragment

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.applink.RouteManager
import com.tokopedia.content.common.navigation.shorts.PlayShorts
import com.tokopedia.content.common.navigation.shorts.PlayShortsParam
import com.tokopedia.content.common.types.BundleData
import com.tokopedia.imagepicker_insta.analytic.FeedVideoDepreciationAnalytic
import com.tokopedia.imagepicker_insta.di.DaggerImagePickerComponent
import com.tokopedia.imagepicker_insta.di.ImagePickerComponent
import com.tokopedia.media.loader.loadImage
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.UnifyButton
import javax.inject.Inject
import com.tokopedia.imagepicker_insta.R as imagePickerInstaR

/**
 * Created by meyta.taliti on 17/04/23.
 */
class FeedVideoDepreciationBottomSheet : BottomSheetUnify() {

    @Inject lateinit var analytic: FeedVideoDepreciationAnalytic

    init {
        showKnob = true
        showHeader = false
        showCloseIcon = false
    }

    private val daggerComponent: ImagePickerComponent by lazy {
        DaggerImagePickerComponent.builder()
            .baseAppComponent(
                (requireContext().applicationContext as BaseMainApplication).baseAppComponent
            )
            .build()
    }

    private val asBuyer: Boolean
        get() {
            return if (arguments == null) false else {
                requireArguments().getBoolean(BundleData.IS_CREATE_POST_AS_BUYER)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        injectFragment()
        super.onCreate(savedInstanceState)
        setChild(
            View.inflate(
                requireContext(),
                imagePickerInstaR.layout.feed_video_depreciate_bottomsheet,
                null
            )
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<ImageView>(imagePickerInstaR.id.img_feed_new_video_uploader)
            .loadImage(
                getString(imagePickerInstaR.string.feed_new_video_uploader)
            )

        view.findViewById<UnifyButton>(imagePickerInstaR.id.btn_feed_new_video_uploader)
            .setOnClickListener {
                analytic.sendClickToShortVideoEvent(asBuyer)
                goToPlayShort()
            }

        view.findViewById<UnifyButton>(imagePickerInstaR.id.btn_feed_ok)
            .setOnClickListener {
                analytic.sendClickOkCloseBottomSheetEvent(asBuyer)
                dismiss()
            }
    }

    private fun injectFragment() {
        daggerComponent.inject(this)
    }

    private fun goToPlayShort() {
        val appLink = PlayShorts.generateApplink {
            setAuthorType(
                if (asBuyer) PlayShortsParam.AuthorType.User else PlayShortsParam.AuthorType.Shop
            )
        }
        RouteManager.route(requireContext(), appLink)
    }

    companion object {

        fun newInstance(asBuyer: Boolean): FeedVideoDepreciationBottomSheet {
            return FeedVideoDepreciationBottomSheet().apply {
                arguments = Bundle().apply {
                    putBoolean(BundleData.IS_CREATE_POST_AS_BUYER, asBuyer)
                }
            }
        }
    }
}
