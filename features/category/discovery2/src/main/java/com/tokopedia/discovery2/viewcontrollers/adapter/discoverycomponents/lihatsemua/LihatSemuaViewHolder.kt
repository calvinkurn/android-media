package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.lihatsemua

import android.graphics.Color
import android.graphics.Outline
import android.os.Build
import android.view.View
import android.view.ViewOutlineProvider
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.tokopedia.discovery2.ComponentNames
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.setTextAndCheckShow
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toZeroIfNull
import com.tokopedia.media.loader.loadImage
import com.tokopedia.media.loader.loadImageFitCenter
import com.tokopedia.media.loader.loadImageWithoutPlaceholder
import com.tokopedia.unifyprinciples.Typography
import java.lang.Exception

class LihatSemuaViewHolder(itemView: View, private val fragment: Fragment) : AbstractViewHolder(itemView, fragment.viewLifecycleOwner) {

    private lateinit var lihatSemuaViewModel: LihatSemuaViewModel
    private var lihatTextView: Typography = itemView.findViewById(R.id.lihat_semua_tv)
    private var lihatTitleTextView: Typography = itemView.findViewById(R.id.title_tv)
    private var lihatSubTitleTextView: Typography = itemView.findViewById(R.id.sub_header_tv)
    private var backgroundImageView: ImageView = itemView.findViewById(R.id.bg_iv)
    private var titleImageView: ImageView = itemView.findViewById(R.id.title_iv)
    private var titleImageViewParent: FrameLayout = itemView.findViewById(R.id.title_iv_parent)
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
                lihatTextView.setOnClickListener {
                    navigateToAppLink(data)
                    sendGtmEvent(componentItem)
                }
            }
        })
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
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                backgroundImageView.outlineProvider = object : ViewOutlineProvider() {

                    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
                    override fun getOutline(view: View?, outline: Outline?) {
                        outline?.setRoundRect(-100, 0, (view?.width).toZeroIfNull(), (view?.height).toZeroIfNull(), 100f)
                    }
                }

                backgroundImageView.clipToOutline = true

            }
            backgroundImageView.show()
        }catch (e:Exception){
            backgroundImageView.hide()
        }
    }

    private fun setupTitleImage(data: DataItem){
        if(data.imageTitle.isNullOrEmpty()) {
            titleImageViewParent.hide()
        }else{
            titleImageView.loadImage(data.imageTitle)
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
}