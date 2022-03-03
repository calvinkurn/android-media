package com.tokopedia.tokomember

import android.content.res.Resources
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ViewFlipper
import com.tokopedia.applink.RouteManager
import com.tokopedia.media.loader.loadImage
import com.tokopedia.tokomember.TokomemberActivity.Companion.KEY_MEMBERSHIP
import com.tokopedia.tokomember.di.DaggerTokomemberComponent
import com.tokopedia.tokomember.model.BottomSheetContentItem
import com.tokopedia.tokomember.trackers.TokomemberTracker
import com.tokopedia.tokomember.util.MembershipWidgetType.Companion.MEMBERSHIP_OPEN
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.toDp
import com.tokopedia.unifyprinciples.Typography

class TokomemberBottomSheetView : BottomSheetUnify() {

    private var onFinishedListener: OnFinishedListener? = null
    private var textTitle: Typography? = null
    private var textDesc: Typography? = null
    private var button: UnifyButton? = null
    private var imageShop: ImageUnify? = null
    private var viewContainer: ViewFlipper? = null
    private var bottomSheetData: BottomSheetContentItem? = null

    companion object {
        fun newInstance(bundle: Bundle): TokomemberBottomSheetView {
            val rbs = TokomemberBottomSheetView()
            rbs.arguments = bundle
            return rbs
        }

        const val SHIMMER = 0
        const val DATA = 1
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        init()
        return super.onCreateView(inflater, container, savedInstanceState)
    }


    private fun init() {
        val view = View.inflate(context, R.layout.tm_bottomsheet_container, null)
        viewContainer = view.findViewById(R.id.viewContainer)
        textTitle = view.findViewById(R.id.tvTitle)
        textDesc = view.findViewById(R.id.tvSubtitle)
        button = view?.findViewById(R.id.tokoButton)
        imageShop = view?.findViewById(R.id.imageOne)
        viewContainer?.displayedChild = SHIMMER
        setUpBottomSheet()
        setChild(view)

        DaggerTokomemberComponent.builder()
            .build().inject(this)
    }

    private fun setUpBottomSheet() {
        this.apply {
            isDragable = true
            isHideable = true
            showKnob = false
            showCloseIcon = true
            clearContentPadding = true
            bottomSheet.isGestureInsetBottomIgnored = true
            customPeekHeight = (Resources.getSystem().displayMetrics.heightPixels / 2).toDp()

        }
        bottomSheetData = arguments?.getParcelable(KEY_MEMBERSHIP)
        if (bottomSheetData != null) {
            viewContainer?.displayedChild = DATA
            setCloseClickListener {
                TokomemberTracker().closeMainBottomSheet(
                    bottomSheetData?.membershipType ?: 0,
                    bottomSheetData?.shopID?.toString() ?: "",
                    bottomSheetData?.paymentID ?: "", bottomSheetData?.source ?: 0
                )
                dismiss()
                onFinishedListener?.onFinish()
            }
            setBottomSheetData()
        }
    }

    private fun setBottomSheetData() {
        textTitle?.text = Html.fromHtml(bottomSheetData?.title ?: "")
        textDesc?.text = bottomSheetData?.description
        button?.text = bottomSheetData?.cta?.text
        imageShop?.loadImage(bottomSheetData?.imageURL)

        button?.setOnClickListener {
            TokomemberTracker().userClickBottomSheetButton(
                bottomSheetData?.membershipType ?: 0,
                bottomSheetData?.shopID?.toString() ?: "",
                bottomSheetData?.paymentID ?: "", bottomSheetData?.source ?: 0
            )
            if (bottomSheetData?.membershipType == MEMBERSHIP_OPEN) {
                RouteManager.route(context, bottomSheetData?.cta?.appLink)
            } else {
                this.dismiss()
            }
        }
    }

    fun setOnFinishedListener(onFinishedListener: OnFinishedListener) {
        this.onFinishedListener = onFinishedListener
    }

    interface OnFinishedListener {
        fun onFinish()
    }
}