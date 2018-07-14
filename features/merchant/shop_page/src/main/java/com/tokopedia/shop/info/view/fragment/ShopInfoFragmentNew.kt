package com.tokopedia.shop.info.view.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.BaseEmptyViewHolder
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.widget.DividerItemDecoration
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.shop.R
import com.tokopedia.shop.common.data.source.cloud.model.ShopInfo
import com.tokopedia.shop.common.di.component.ShopComponent
import com.tokopedia.shop.extension.transformToVisitable
import com.tokopedia.shop.info.di.component.DaggerShopInfoComponent
import com.tokopedia.shop.info.di.module.ShopInfoModule
import com.tokopedia.shop.info.view.adapter.ShopInfoLogisticAdapter
import com.tokopedia.shop.info.view.adapter.ShopInfoLogisticAdapterTypeFactory
import com.tokopedia.shop.info.view.listener.ShopInfoView
import com.tokopedia.shop.info.view.presenter.ShopInfoPresenter
import com.tokopedia.shop.note.view.adapter.ShopNoteAdapterTypeFactory
import com.tokopedia.shop.note.view.model.ShopNoteViewModel
import kotlinx.android.synthetic.main.fragment_shop_info_v3.*
import kotlinx.android.synthetic.main.partial_shop_info_description.*
import kotlinx.android.synthetic.main.partial_shop_info_logistic_2.*
import kotlinx.android.synthetic.main.partial_shop_info_note.*
import kotlinx.android.synthetic.main.partial_shop_info_statistics.*
import javax.inject.Inject

class ShopInfoFragmentNew: BaseDaggerFragment(), ShopInfoView, BaseEmptyViewHolder.Callback {

    companion object {
        @JvmStatic fun createInstance(): Fragment = ShopInfoFragmentNew()
    }

    @Inject lateinit var presenter: ShopInfoPresenter
    private val noteAdapter = BaseListAdapter<ShopNoteViewModel, ShopNoteAdapterTypeFactory>(ShopNoteAdapterTypeFactory())
    private var shopId: String = "0"

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_shop_info_v3, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerViewNote.isNestedScrollingEnabled = false
        recyclerViewNote.isFocusable = false
        recyclerViewLogistic.isNestedScrollingEnabled = false
        recyclerViewLogistic.isFocusable = false

        recyclerViewNote.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        recyclerViewNote.setHasFixedSize(true)

        recyclerViewLogistic.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        recyclerViewLogistic.setHasFixedSize(true)
        recyclerViewLogistic.addItemDecoration(DividerItemDecoration(activity))
    }

    override fun onDestroy() {
        presenter.detachView()
        super.onDestroy()
    }

    fun updateShopInfo(shopInfo: ShopInfo){
        shopId = shopInfo.info.shopId
        displayImageBackground(shopInfo)
        displayShopDescription(shopInfo)
        displayShopStatistics(shopInfo)
        displayShopLogistic(shopInfo)
        displayShopNote()

    }

    private fun displayShopNote() {
        recyclerViewNote.adapter = noteAdapter
        showLoading()
        presenter.getShopNoteList(shopId)
    }

    private fun showLoading() {
        noteAdapter.removeErrorNetwork()
        recyclerViewNote.visibility = View.GONE
        loading.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        loading.visibility = View.GONE
        recyclerViewNote.visibility = View.VISIBLE
    }

    private fun displayShopLogistic(shopInfo: ShopInfo) {
        recyclerViewLogistic.adapter = ShopInfoLogisticAdapter(ShopInfoLogisticAdapterTypeFactory(),
                shopInfo.shipment.map { it.transformToVisitable() })
    }

    private fun displayShopStatistics(shopInfo: ShopInfo) {
        productQualityValue.text = shopInfo.ratings.quality.average
        productRating.rating = try {
            shopInfo.ratings.quality.average.toFloat()
        } catch (e: Exception) {
            shopInfo.ratings.quality.ratingStar
        }
        totalReview.text = getString(R.string.shop_info_content_total_review, shopInfo.ratings.quality.countTotal)
        textSeeRating.setOnClickListener { goToReviewQualityDetail() }
    }

    private fun displayShopDescription(shopInfo: ShopInfo) {
        shopInfoDescription.text = shopInfo.info.shopDescription
        shopInfoLocation.text = shopInfo.info.shopLocation
        shopInfoOpenSince.text = getString(R.string.shop_info_label_open_since_v3, shopInfo.info.shopOpenSince)

        val reputaionMedalType = shopInfo.stats.shopBadgeLevel.set.toInt()
        val reputationLevel = shopInfo.stats.shopBadgeLevel.level.toInt()
        val reputationScore = shopInfo.stats.shopReputationScore

        shopReputationView.setValue(reputaionMedalType, reputationLevel, reputationScore)
        totalPoin.text = getString(R.string.dashboard_x_points, reputationScore)

        textViewScoreGood.text = shopInfo.stats.shopLastTwelveMonths.countScoreGood
        textViewScoreNeutral.text = shopInfo.stats.shopLastTwelveMonths.countScoreNeutral
        textViewScoreBad.text  = shopInfo.stats.shopLastTwelveMonths.countScoreBad
    }

    private fun displayImageBackground(shopInfo: ShopInfo) {
        ImageHandler.LoadImage(shopBackgroundImageView, shopInfo.info.shopCover)
    }

    private fun goToReviewQualityDetail() {

    }

    override fun getScreenName(): String? = null

    override fun initInjector() {
        DaggerShopInfoComponent.builder().shopInfoModule(ShopInfoModule())
                .shopComponent(getComponent(ShopComponent::class.java))
                .build().inject(this)
        presenter.attachView(this)
    }

    override fun renderListNote(notes: List<ShopNoteViewModel>) {
        hideLoading()
        noteAdapter.clearAllElements()
        noteAdapter.addElement(notes)
        if (notes.isEmpty()){
            noteAdapter.addElement(EmptyModel().apply {
                if (presenter.isMyshop(shopId)) {
                    title = getString(R.string.shop_note_empty_note_title_seller)
                    callback = this@ShopInfoFragmentNew
                } else {
                    title = getString(R.string.shop_note_empty_note_title_buyer)
                }
            })
            noteLabelView.setContent("")
        }
    }

    override fun showListNoteError(throwable: Throwable?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onEmptyContentItemTextClicked() {}

    override fun onEmptyButtonClicked() {

    }
}