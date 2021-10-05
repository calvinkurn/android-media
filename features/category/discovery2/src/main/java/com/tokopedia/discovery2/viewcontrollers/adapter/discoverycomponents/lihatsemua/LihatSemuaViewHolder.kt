package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.lihatsemua

import android.graphics.Color
import android.graphics.Outline
import android.os.Build
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.discovery2.ComponentNames
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.Utils
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.media.loader.loadImage
import com.tokopedia.media.loader.loadImageFitCenter
import com.tokopedia.media.loader.loadImageWithoutPlaceholder
import com.tokopedia.unifycomponents.TimerUnify
import com.tokopedia.unifycomponents.timer.TimerUnifySingle
import com.tokopedia.unifyprinciples.Typography
import java.lang.Exception

class LihatSemuaViewHolder(itemView: View, private val fragment: Fragment) : AbstractViewHolder(itemView, fragment.viewLifecycleOwner) {

    private lateinit var lihatSemuaViewModel: LihatSemuaViewModel
    private var lihatTextView: Typography = itemView.findViewById(R.id.lihat_semua_tv)
    private var lihatTitleTextView: Typography = itemView.findViewById(R.id.title_tv)
    private var lihatSubTitleTextView: Typography = itemView.findViewById(R.id.sub_header_tv)
    private var backgroundImageView: ImageView = itemView.findViewById(R.id.bg_iv)
    private var titleImageView: ImageView = itemView.findViewById(R.id.title_iv)
    private var titleImageViewParent: ViewGroup = itemView.findViewById(R.id.title_iv_parent)
    private var timer:TimerUnifySingle = itemView.findViewById(R.id.timer_lihat_semua)
    private var onLihatSemuaClickListener: OnLihatSemuaClickListener? = null

    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        lihatSemuaViewModel = discoveryBaseViewModel as LihatSemuaViewModel
    }

    override fun setUpObservers(lifecycleOwner: LifecycleOwner?) {
        super.setUpObservers(lifecycleOwner)
        lifecycleOwner?.let {
            lihatSemuaViewModel.getComponentData().observe(it, Observer { componentItem ->
            componentItem.data?.firstOrNull()?.let { data ->
                lihatTitleTextView.setTextAndCheckShow(data.title)
                lihatSubTitleTextView.setTextAndCheckShow(data.subtitle)
                if (data.btnApplink.isNullOrEmpty()) {
                    lihatTextView.hide()
                } else {
                    lihatTextView.show()
                }
                setupTitleImage(data)
                setupBackgroundImage(data)
                setupTextColours(backgroundImageView.isVisible)
                setupPaddingForTexts(backgroundImageView.isVisible,titleImageViewParent.isVisible)
                setupTimer(data)
                lihatTextView.setOnClickListener {
                    navigateToAppLink(data)
                    sendGtmEvent(componentItem)
                }
            }
        })
        }
    }

    private fun setupTimer(data: DataItem) {
        if(!data.endTime.isNullOrEmpty()&&!data.startTime.isNullOrEmpty()){
            lihatSemuaViewModel.startTimer(timer)
            timer.show()
        }else{
            timer.hide()
        }
    }

    private fun setupTextColours(backgroundPresent: Boolean) {
        fragment.context?.let {
//            if (backgroundPresent) {
////              Todo::  Change to dms_colour for dark mode plugin support
//                lihatTitleTextView.setTextColor(MethodChecker.getColor(it,R.color.white))
//                lihatSubTitleTextView.setTextColor(MethodChecker.getColor(it,R.color.white_95))
//            } else {
//                lihatTitleTextView.setTextColor(MethodChecker.getColor(it,R.color.Unify_N700_96))
//                lihatSubTitleTextView.setTextColor(MethodChecker.getColor(it,R.color.Unify_N700_68))
//            }
        }
    }

    override fun removeObservers(lifecycleOwner: LifecycleOwner?) {
        super.removeObservers(lifecycleOwner)
        lifecycleOwner?.let {
            lihatSemuaViewModel.getComponentData().removeObservers(it)
        }
    }

    private fun setupBackgroundImage(data: DataItem){
        if(data.boxColor.isNullOrEmpty() && data.backgroundImageUrl.isNullOrEmpty()) {
            backgroundImageView.hide()
            return
        }
        try {
            if(!data.boxColor.isNullOrEmpty())
                backgroundImageView.setBackgroundColor(Color.parseColor(data.boxColor))
            if(!data.backgroundImageUrl.isNullOrEmpty())
                backgroundImageView.loadImageWithoutPlaceholder(data.backgroundImageUrl)
            corners(backgroundImageView,-100, 0, 0,0, 100f)
            backgroundImageView.show()
        }catch (e:Exception){
            backgroundImageView.hide()
        }
    }

    private fun setupPaddingForTexts(isBackgroundPresent:Boolean,isTitleImagePresent:Boolean) {
        fragment.context?.resources?.let {
            if(isBackgroundPresent) {
                lihatTitleTextView.setPadding(
                        if (isTitleImagePresent) it.getDimensionPixelOffset(R.dimen.dp_12) else it.getDimensionPixelOffset(R.dimen.dp_16),
                        it.getDimensionPixelOffset(R.dimen.dp_8),
                        0,
                        if (lihatSubTitleTextView.isVisible) 0 else it.getDimensionPixelOffset(R.dimen.dp_8))
                lihatSubTitleTextView.setPadding(
                        if (isTitleImagePresent) it.getDimensionPixelOffset(R.dimen.dp_12) else it.getDimensionPixelOffset(R.dimen.dp_16),
                        0,
                        0,
                        it.getDimensionPixelOffset(R.dimen.dp_8))
                lihatTextView.setPadding(0,0,0,it.getDimensionPixelOffset(R.dimen.dp_8))
            }else{
//              TODO:: Handle cases for non background case
                lihatTitleTextView.setPadding(
                        if (isTitleImagePresent) it.getDimensionPixelOffset(R.dimen.dp_4) else it.getDimensionPixelOffset(R.dimen.dp_16),
                        if(isTitleImagePresent)it.getDimensionPixelOffset(R.dimen.dp_8) else 0,
                        0,
                        if (lihatSubTitleTextView.isVisible || !isTitleImagePresent) 0 else it.getDimensionPixelOffset(R.dimen.dp_8))
                lihatSubTitleTextView.setPadding(
                        if (isTitleImagePresent) it.getDimensionPixelOffset(R.dimen.dp_4) else it.getDimensionPixelOffset(R.dimen.dp_16),
                        0,
                        0,
                        if(isTitleImagePresent) it.getDimensionPixelOffset(R.dimen.dp_8) else 0)
//                Todo:: Bottom padding check
                lihatTextView.setPadding(0,
                        0,
                        0,
                        if (!isTitleImagePresent) 0 else it.getDimensionPixelOffset(R.dimen.dp_8))
            }
        }

    }

    private fun setupTitleImage(data: DataItem){
        if(data.imageTitle.isNullOrEmpty()) {
            titleImageViewParent.hide()
        }else{
            titleImageView.loadImage(data.imageTitle)
            val lp = titleImageView.layoutParams
            val height = Utils.extractDimension(data.imageTitle,"height")
            val width = Utils.extractDimension(data.imageTitle,"width")
            if (width != null && height != null) {
                val aspectRatio = width.toFloat() / height.toFloat()
                fragment.context?.resources?.let {
                    corners(titleImageView,0,0,0,0,it.getDimension(R.dimen.dp_8))
                    if (lihatSubTitleTextView.isVisible) {
                        lp.height = it.getDimensionPixelOffset(R.dimen.dp_56)
                        lp.width = (aspectRatio * it.getDimensionPixelOffset(R.dimen.dp_56)).toInt()
                    } else {
                        lp.height = it.getDimensionPixelOffset(R.dimen.dp_44)
                        lp.width = (aspectRatio * it.getDimensionPixelOffset(R.dimen.dp_44)).toInt()
                    }
                }
            }
            titleImageView.layoutParams = lp
            titleImageViewParent.show()
        }
    }

    private fun sendGtmEvent(componentsItem: ComponentsItem) {
        if (componentsItem.name == ComponentNames.ProductCardCarousel.componentName) {
            onLihatSemuaClickListener?.onProductCardHeaderClick(componentsItem)
        } else {
            componentsItem.data?.first()?.let {
                onLihatSemuaClickListener?.onLihatSemuaClick(it)
            }
        }
    }

    private fun navigateToAppLink(data: DataItem) {
        lihatSemuaViewModel.navigate(fragment.activity, data.btnApplink)
    }

    interface OnLihatSemuaClickListener {
        fun onProductCardHeaderClick(componentsItem: ComponentsItem)
        fun onLihatSemuaClick(data: DataItem)
    }

    override fun onViewAttachedToWindow() {
        super.onViewAttachedToWindow()
        if(fragment is OnLihatSemuaClickListener){
            onLihatSemuaClickListener = fragment
        }
    }

    fun corners(view:View,leftOffset:Int, topOffset:Int, rightOffset:Int, bottomOffset:Int, radius:Float){
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                view.outlineProvider = object : ViewOutlineProvider() {
                    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
                    override fun getOutline(view: View?, outline: Outline?) {
                        outline?.setRoundRect(leftOffset, topOffset, (view?.width).toZeroIfNull()+rightOffset, (view?.height).toZeroIfNull()+ bottomOffset, radius)
                    }
                }
                view.clipToOutline = true
            }
        }catch (e:Exception){

        }
    }
}