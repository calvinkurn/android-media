package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.lihatsemua

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Outline
import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.discovery2.ComponentNames
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.Utils
import com.tokopedia.discovery2.Utils.Companion.TIMER_DATE_FORMAT
import com.tokopedia.discovery2.Utils.Companion.toDecodedString
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.discovery2.viewcontrollers.fragment.DiscoveryFragment
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.media.loader.loadImage
import com.tokopedia.media.loader.loadImageWithoutPlaceholder
import com.tokopedia.unifycomponents.timer.TimerUnifySingle
import com.tokopedia.unifyprinciples.Typography
import java.net.URLDecoder
import java.util.*

class LihatSemuaViewHolder(itemView: View, private val fragment: Fragment) : AbstractViewHolder(itemView, fragment.viewLifecycleOwner) {

    private lateinit var lihatSemuaViewModel: LihatSemuaViewModel
    private var lihatTextView: Typography = itemView.findViewById(R.id.lihat_semua_tv)
    private var lihatImageView: ImageView = itemView.findViewById(R.id.lihat_semua_iv)
    private var lihatTitleTextView: Typography = itemView.findViewById(R.id.title_tv)
    private var lihatSubTitleTextView: Typography = itemView.findViewById(R.id.sub_header_tv)
    private var backgroundImageView: ImageView = itemView.findViewById(R.id.bg_iv)
    private var titleImageView: ImageView = itemView.findViewById(R.id.title_iv)
    private var titleImageViewParent: ViewGroup = itemView.findViewById(R.id.title_iv_parent)
    private var textTitleParent:ViewGroup = itemView.findViewById(R.id.text_data_parent)
    private var timer:TimerUnifySingle = itemView.findViewById(R.id.timer_lihat_semua)
    private var onLihatSemuaClickListener: OnLihatSemuaClickListener? = null

    init {
        TimeZone.setDefault(TimeZone.getTimeZone(Utils.TIME_ZONE))
    }

    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        lihatSemuaViewModel = discoveryBaseViewModel as LihatSemuaViewModel
    }

    override fun setUpObservers(lifecycleOwner: LifecycleOwner?) {
        super.setUpObservers(lifecycleOwner)
        lifecycleOwner?.let {
            lihatSemuaViewModel.getComponentData().observe(it, Observer { componentItem ->
                componentItem.data?.firstOrNull()?.let { data ->
                    lihatTitleTextView.setTextAndCheckShow(data.title)
                    setupSubtitle(data)
                    setupTitleImage(data)
                    setupBackgroundImage(data)
                    setupLihat(data, componentItem, backgroundImageView.isVisible)
                    setupTextColours(backgroundImageView.isVisible)
                    setupTimer(data,backgroundImageView.isVisible)
                    setupPadding(backgroundImageView.isVisible, titleImageViewParent.isVisible)
                }
            })
            lihatSemuaViewModel.getRestartTimerAction().observe(it, { shouldStartTimer ->
                if (shouldStartTimer && (lihatSemuaViewModel.getStartDate().isNotEmpty() || lihatSemuaViewModel.getEndDate().isNotEmpty())) {
                    lihatSemuaViewModel.startTimer(timer)
                }
            })
        }
    }

    private fun setupLihat(data: DataItem,componentsItem: ComponentsItem, backgroundPresent: Boolean) {
        if (!data.btnApplink.isNullOrEmpty()) {
            if(backgroundPresent) {
                lihatImageView.show()
                lihatTextView.hide()
            }else{
                lihatTextView.show()
                lihatImageView.hide()
            }
        } else {
            lihatImageView.hide()
            lihatTextView.hide()
        }
        lihatTextView.setOnClickListener {
            navigateLihat(data,componentsItem)
        }
        lihatImageView.setOnClickListener {
            navigateLihat(data,componentsItem)
        }
    }

    private fun navigateLihat(data: DataItem,componentsItem: ComponentsItem){
        navigateToAppLink(data)
        sendGtmEvent(componentsItem)
    }

    private fun setupTimer(data: DataItem,isBackgroundPresent: Boolean) {
        if (!data.endDate.isNullOrEmpty() || !data.startDate.isNullOrEmpty()) {
            timer.timerVariant = if(isBackgroundPresent) TimerUnifySingle.VARIANT_ALTERNATE else TimerUnifySingle.VARIANT_MAIN
            lihatSemuaViewModel.startTimer(timer)
            timer.show()
        } else {
            timer.hide()
        }
    }

    private fun setupSubtitle(data: DataItem){
        var subtitle = data.subtitle?.toDecodedString()
        if(!data.endDate.isNullOrEmpty() || !data.startDate.isNullOrEmpty()){
            if(!Utils.isFutureSale(data.startDate?:"",TIMER_DATE_FORMAT) && !data.subtitle_1.isNullOrEmpty()){
                subtitle = data.subtitle_1
            }
        }
        if(Utils.isSaleOver(data.endDate?:"", TIMER_DATE_FORMAT) && subtitle.equals(BERAKHIR_DALAM,ignoreCase = true)){
            subtitle = SUDAH_BERAKHIR
        }
        lihatSubTitleTextView.setTextAndCheckShow(subtitle)
    }

    private fun setupTextColours(backgroundPresent: Boolean) {
        fragment.context?.let {
            if (backgroundPresent) {
                lihatTitleTextView.setTextColor(MethodChecker.getColor(it,R.color.discovery2_dms_white))
                lihatSubTitleTextView.setTextColor(MethodChecker.getColor(it,R.color.discovery2_dms_white_95))
            } else {
                lihatTitleTextView.setTextColor(MethodChecker.getColor(it,com.tokopedia.unifyprinciples.R.color.Unify_N700_96))
                lihatSubTitleTextView.setTextColor(MethodChecker.getColor(it,com.tokopedia.unifyprinciples.R.color.Unify_N700_68))
            }
        }
    }

    override fun removeObservers(lifecycleOwner: LifecycleOwner?) {
        super.removeObservers(lifecycleOwner)
        lifecycleOwner?.let {
            lihatSemuaViewModel.getComponentData().removeObservers(it)
            lihatSemuaViewModel.getRestartTimerAction().removeObservers(it)
        }
    }

    private fun setupBackgroundImage(data: DataItem){
        if(data.boxColor.isNullOrEmpty() && data.backgroundImageUrl.isNullOrEmpty()) {
            backgroundImageView.hide()
            return
        }
        try {
            if (!data.boxColor.isNullOrEmpty())
                if (titleImageViewParent.isVisible) {
                    backgroundImageView.background = ContextCompat.getDrawable(itemView.context, R.drawable.disco_lihat_semua_title_bg)
                    ViewCompat.setBackgroundTintList(backgroundImageView,ColorStateList.valueOf(Color.parseColor(data.boxColor)))
                } else {
                    backgroundImageView.setBackgroundColor(Color.parseColor(data.boxColor))
                }
            if (!data.backgroundImageUrl.isNullOrEmpty())
                backgroundImageView.loadImageWithoutPlaceholder(data.backgroundImageUrl)
            cornersForBackground(backgroundImageView)
            backgroundImageView.show()
        } catch (e: Exception) {
            backgroundImageView.hide()
        }
    }

    private fun setupPadding(isBackgroundPresent: Boolean, isTitleImagePresent: Boolean) {
        fragment.context?.resources?.let {
            if (isBackgroundPresent) {
                textTitleParent.setPadding(
                        if (isTitleImagePresent) it.getDimensionPixelOffset(R.dimen.dp_12) else it.getDimensionPixelOffset(R.dimen.dp_16),
                        if (isTitleImagePresent) it.getDimensionPixelOffset(R.dimen.dp_8) else it.getDimensionPixelOffset(R.dimen.dp_6),
                        it.getDimensionPixelOffset(R.dimen.dp_26),
                        it.getDimensionPixelOffset(R.dimen.dp_8)
                )
                lihatTextView.setPadding(
                        0,
                        0,
                        0,
                        it.getDimensionPixelOffset(R.dimen.dp_8)
                )
            } else {
                textTitleParent.setPadding(
                        if (isTitleImagePresent) it.getDimensionPixelOffset(R.dimen.dp_4) else it.getDimensionPixelOffset(
                                R.dimen.dp_16
                        ),
                        if (isTitleImagePresent) it.getDimensionPixelOffset(R.dimen.dp_8) else 0,
                        it.getDimensionPixelOffset(R.dimen.dp_26),
                        if (isTitleImagePresent) it.getDimensionPixelOffset(R.dimen.dp_8) else 0
                )
                lihatTextView.setPadding(
                        0,
                        0,
                        0,
                        if (!isTitleImagePresent) 0 else it.getDimensionPixelOffset(R.dimen.dp_8)
                )
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
                    Utils.corners(titleImageViewParent,0 - it.getDimension(R.dimen.dp_12).toInt(),0,0,0 + it.getDimension(R.dimen.dp_12).toInt(),it.getDimension(R.dimen.dp_12))
                    Utils.corners(titleImageView,0,0,0,0,it.getDimension(R.dimen.dp_8))
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
        when (componentsItem.name) {
            ComponentNames.ProductCardCarousel.componentName -> {
                onLihatSemuaClickListener?.onProductCardHeaderClick(componentsItem)
            }
            ComponentNames.MerchantVoucherCarousel.componentName -> {
                (fragment as DiscoveryFragment).getDiscoveryAnalytics().trackMerchantVoucherLihatSemuaClick(componentsItem.data?.firstOrNull())
            }
            ComponentNames.CLPFeaturedProducts.componentName -> {
                (fragment as DiscoveryFragment).getDiscoveryAnalytics().trackPromoLihat(componentsItem)
            }
            else -> {
                componentsItem.data?.first()?.let {
                    onLihatSemuaClickListener?.onLihatSemuaClick(it)
                }
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
        if (fragment is OnLihatSemuaClickListener) {
            onLihatSemuaClickListener = fragment
        }
        timer.onFinish = {
            lihatSemuaViewModel.timerWithBannerCounter = null
            lihatSemuaViewModel.component.data?.firstOrNull()?.let {
                setupSubtitle(it)
            }
            lihatSemuaViewModel.startTimer(timer)
        }
    }

    override fun onViewDetachedToWindow() {
        super.onViewDetachedToWindow()
        lihatSemuaViewModel.stopTimer()
        timer.pause()
    }

    fun cornersForBackground(view:View){
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                view.outlineProvider = object : ViewOutlineProvider() {
                    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
                    override fun getOutline(view: View?, outline: Outline?) {
                        var finalRadius = 100f
                            if(((view?.height).toZeroIfNull()) != 0) {
                               finalRadius =  view!!.height.div(2).toFloat()
                            }
                        outline?.setRoundRect(-finalRadius.toInt(), 0, (view?.width).toZeroIfNull(), (view?.height).toZeroIfNull(),finalRadius)
                    }
                }
                view.clipToOutline = true
            }
        }catch (e:Exception){

        }
    }

    companion object{
        const val BERAKHIR_DALAM = "Berakhir dalam"
        const val SUDAH_BERAKHIR = "Sudah berakhir"
    }
}
