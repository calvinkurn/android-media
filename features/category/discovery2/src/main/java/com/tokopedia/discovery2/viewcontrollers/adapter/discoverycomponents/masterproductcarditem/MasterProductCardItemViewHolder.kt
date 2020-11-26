package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.masterproductcarditem

import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.tokopedia.discovery2.ComponentNames
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.discovery2.viewcontrollers.fragment.DiscoveryFragment
import com.tokopedia.productcard.ProductCardGridView
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.UnifyButton

class MasterProductCardItemViewHolder(itemView: View, val fragment: Fragment) : AbstractViewHolder(itemView, fragment.viewLifecycleOwner) {

    private lateinit var masterProductCardItemViewModel: MasterProductCardItemViewModel
    private var masterProductCard: ProductCardGridView = itemView.findViewById(R.id.master_product_card)
    private var productCardView: CardView = itemView.findViewById(R.id.cardViewProductCard)
    private var productCardName = ""
    private var dataItem: DataItem? = null
    private var componentPosition: Int? = null
    private var buttonNotify: UnifyButton? = null

    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        masterProductCardItemViewModel = discoveryBaseViewModel as MasterProductCardItemViewModel
        initView()
    }

    private fun initView() {
        buttonNotify = masterProductCard.getNotifyMeButton()
        masterProductCard.setNotifyMeOnClickListener {
            masterProductCardItemViewModel.subscribeUser()
        }
        productCardView.setOnClickListener {
            handleUIClick(it)
        }
    }

    override fun setUpObservers(lifecycleOwner: LifecycleOwner?) {
        super.setUpObservers(lifecycleOwner)
        productCardName = masterProductCardItemViewModel.getComponentName()
        lifecycleOwner?.let {
            masterProductCardItemViewModel.getDataItemValue().observe(lifecycleOwner, Observer { data ->
                dataItem = data
            })
            masterProductCardItemViewModel.getProductModelValue().observe(lifecycleOwner, Observer { data ->
                populateData(data)
            })
            masterProductCardItemViewModel.getComponentPosition().observe(lifecycleOwner, Observer { position ->
                componentPosition = position
            })
            masterProductCardItemViewModel.getShowLoginData().observe(lifecycleOwner, Observer {
                if (it == true) {
                    componentPosition?.let { position -> (fragment as DiscoveryFragment).openLoginScreen(position) }
                }
            })
            masterProductCardItemViewModel.notifyMeCurrentStatus().observe(lifecycleOwner, Observer {
                updateNotifyMeState(it)
            })
            masterProductCardItemViewModel.showNotifyToastMessage().observe(lifecycleOwner, Observer {
                showNotifyResultToast(it)
            })
            masterProductCardItemViewModel.getComponentPosition().observe(lifecycleOwner, Observer {
                componentPosition = it
            })
            masterProductCardItemViewModel.getSyncPageLiveData().observe(lifecycleOwner, Observer {
                if (it) (fragment as DiscoveryFragment).reSync()
            })
        }
    }

    override fun removeObservers(lifecycleOwner: LifecycleOwner?) {
        super.removeObservers(lifecycleOwner)
        lifecycleOwner?.let {
            masterProductCardItemViewModel.getDataItemValue().removeObservers(it)
            masterProductCardItemViewModel.getProductModelValue().removeObservers(it)
            masterProductCardItemViewModel.getComponentPosition().removeObservers(it)
            masterProductCardItemViewModel.getShowLoginData().removeObservers(it)
            masterProductCardItemViewModel.notifyMeCurrentStatus().removeObservers(it)
            masterProductCardItemViewModel.showNotifyToastMessage().removeObservers(it)
            masterProductCardItemViewModel.getComponentPosition().removeObservers(it)
            masterProductCardItemViewModel.getSyncPageLiveData().removeObservers(it)
        }
    }

    private fun populateData(productCardModel: ProductCardModel) {
        if (productCardName == ComponentNames.ProductCardCarouselItem.componentName || productCardName == ComponentNames.ProductCardSprintSaleCarouselItem.componentName) {
            productCardView.layoutParams.width = itemView.context.resources.getDimensionPixelSize(R.dimen.disco_product_card_width)
            masterProductCard.applyCarousel()
            masterProductCard.layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT
            masterProductCard.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
        } else {
            masterProductCard.layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
            masterProductCard.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
        }
        masterProductCard.setProductModel(productCardModel)
        updateNotifyMeState(dataItem?.notifyMe)
    }

    private fun updateNotifyMeState(notifyMeStatus: Boolean?) {
        val notifyText = masterProductCardItemViewModel.getNotifyText(notifyMeStatus)
        buttonNotify?.let {
            if (dataItem?.hasNotifyMe == true) {
                it.text = notifyText
                if (notifyMeStatus == true) {
                    it.apply {
                        setTextColor(context.resources.getColor(com.tokopedia.unifyprinciples.R.color.Neutral_N700_68))
                        buttonVariant = UnifyButton.Variant.GHOST
                        buttonType = UnifyButton.Type.ALTERNATE
                    }
                } else {
                    it.apply {
                        setTextColor(context.resources.getColor(com.tokopedia.unifyprinciples.R.color.Green_G500))
                        buttonVariant = UnifyButton.Variant.GHOST
                        buttonType = UnifyButton.Type.MAIN
                    }
                }
            }
        }
    }

    private fun handleUIClick(view: View) {
        when (view) {
            productCardView -> {
                masterProductCardItemViewModel.sendTopAdsClick()
                masterProductCardItemViewModel.navigate(fragment.context, dataItem?.applinks)
                sendClickEvent()
            }
        }
    }

    private fun sendClickEvent() {
        (fragment as DiscoveryFragment).getDiscoveryAnalytics().trackProductCardClick(masterProductCardItemViewModel.components, masterProductCardItemViewModel.isUserLoggedIn())
    }

    override fun onViewAttachedToWindow() {
        super.onViewAttachedToWindow()
        masterProductCardItemViewModel.sendTopAdsView()
        (fragment as DiscoveryFragment).getDiscoveryAnalytics().viewProductsList(masterProductCardItemViewModel.components, masterProductCardItemViewModel.isUserLoggedIn())
    }

    private fun showNotifyResultToast(toastData: Pair<Boolean, String?>) {
        try {
            if (!toastData.first && !toastData.second.isNullOrEmpty()) {
                Toaster.make(itemView.rootView, toastData.second!!, Toaster.LENGTH_SHORT, Toaster.TYPE_NORMAL)
            } else if (toastData.first && !toastData.second.isNullOrEmpty()) {
                Toaster.make(itemView.rootView, toastData.second!!, Toaster.LENGTH_SHORT, Toaster.TYPE_ERROR)
            } else {
                Toaster.make(itemView.rootView, itemView.context.getString(R.string.product_card_error_msg), Toaster.LENGTH_SHORT, Toaster.TYPE_ERROR)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}