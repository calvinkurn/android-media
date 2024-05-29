package com.tokopedia.search.result.product.inspirationlistatc

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.atc_common.data.model.request.AddToCartRequestParams
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartUseCase
import com.tokopedia.discovery.common.constants.SearchConstant.SearchProduct.GET_POST_ATC_CAROUSEL_USE_CASE
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.search.di.scope.SearchScope
import com.tokopedia.search.result.domain.model.SearchProductModel
import com.tokopedia.search.result.domain.model.SearchProductModel.SearchInspirationCarousel
import com.tokopedia.search.result.product.byteio.ByteIOTrackingData
import com.tokopedia.search.result.product.QueryKeyProvider
import com.tokopedia.search.result.product.SearchParameterProvider
import com.tokopedia.search.result.product.ViewUpdater
import com.tokopedia.search.result.product.byteio.ByteIOTrackingDataFactory
import com.tokopedia.search.result.product.inspirationcarousel.InspirationCarouselDataView
import com.tokopedia.search.result.product.inspirationcarousel.analytics.InspirationCarouselTrackingUnificationDataMapper
import com.tokopedia.search.result.product.inspirationlistatc.postatccarousel.InspirationListPostAtcDataView
import com.tokopedia.search.result.product.inspirationlistatc.postatccarousel.InspirationListPostAtcDataView.InspirationListPostAtcDataViewMapper
import com.tokopedia.search.result.product.requestparamgenerator.RequestParamsGenerator
import com.tokopedia.usecase.UseCase
import com.tokopedia.user.session.UserSessionInterface
import rx.Subscriber
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Named

@SearchScope
class InspirationListAtcPresenterDelegate @Inject constructor(
    private val addToCartUseCase: AddToCartUseCase,
    @param:Named(GET_POST_ATC_CAROUSEL_USE_CASE)
    private val getPostATCCarouselUseCase: UseCase<SearchInspirationCarousel>,
    private val requestParamsGenerator: RequestParamsGenerator,
    private val userSession: UserSessionInterface,
    private val inspirationListAtcView: InspirationListAtcView,
    private val viewUpdater: ViewUpdater,
    private val searchParameterProvider: SearchParameterProvider,
    private val byteIOTrackingDataFactory: ByteIOTrackingDataFactory,
): InspirationListAtcPresenter,
    SearchParameterProvider by searchParameterProvider {

    companion object {
        private const val DEFAULT_USER_ID = "0"
        private const val POST_ATC_TYPE= "post_atc"
    }

    override fun onListAtcItemAddToCart(
        product: InspirationCarouselDataView.Option.Product,
        type: String,
    ) {
        inspirationListAtcView.trackAtcClicked(product)
        if (product.shouldOpenVariantBottomSheet()) {
            inspirationListAtcView.trackAddToCartVariant(product)
            if (product.isOrganicAds) inspirationListAtcView.trackAdsClick(product)

            inspirationListAtcView.openVariantBottomSheet(product, type) {
                getPostAtcCarousel(product, type)
            }
        } else {
            executeAtcCommon(product, type)
        }

    }

    private fun isNotPostAtcCarousel(type: String) : Boolean {
        return type != POST_ATC_TYPE
    }

    private fun getPostAtcCarousel(
        product: InspirationCarouselDataView.Option.Product,
        type: String,
    ) {
        if (isNotPostAtcCarousel(type)) {
            val query = searchParameterProvider.getSearchParameter()?.getSearchQuery().orEmpty()
            getPostATCCarouselUseCase.execute(
                requestParamsGenerator.createPostATCCarouselParams(
                    product.shopId,
                    product.categoryID,
                    product.id,
                    product.warehouseID,
                    query
                ),
                object : Subscriber<SearchInspirationCarousel>() {
                    override fun onCompleted() {
                    }

                    override fun onError(e: Throwable?) {
                        Timber.e(e)
                    }

                    override fun onNext(searchInspiration: SearchInspirationCarousel) {
                        val inspirationPostAtc = getInspirationListPostAtcData(searchInspiration)
                        val inspirationListPostAtc =
                            InspirationListPostAtcDataViewMapper.convertToInspirationListPostAtcDataView(
                                byteIOTrackingDataFactory.create(false),
                                product,
                                inspirationPostAtc,
                            )
                        val indexOfListATCSelectedProduct =
                            findSelectedProductAtcPositionOnVisitableList(product)
                        val itemListATCSelectedProduct =
                            getListAtcOnVisitableList(
                                indexOfListATCSelectedProduct
                            )
                        showOrRefreshInspirationPostAtc(
                            indexOfListATCSelectedProduct,
                            itemListATCSelectedProduct ,
                            inspirationListPostAtc,
                        )
                    }
                }
            )
        }
    }

    private fun getInspirationListPostAtcData(
        searchInspiration: SearchInspirationCarousel?
    ): SearchProductModel.InspirationCarouselData? {
        return searchInspiration?.data?.find { it.type == POST_ATC_TYPE }
    }

    private fun findSelectedProductAtcPositionOnVisitableList(
        selectedProduct: InspirationCarouselDataView.Option.Product
    ) : Int {
        return  viewUpdater.itemList?.indexOfFirst {
            it is InspirationListAtcDataView && it.option.product.contains(selectedProduct)
        }.orZero()
    }

    private fun getListAtcOnVisitableList(index: Int): Visitable<*>? {
        return if(index == 0 ) null else viewUpdater.getItemAtIndex(index)
    }

    private fun showOrRefreshInspirationPostAtc(
        indexOfListATCSelectedProduct: Int,
        listATCSelectedProduct: Visitable<*>?,
        listPostAtcProduct: Visitable<*>?,
    ) {
        if(listATCSelectedProduct == null || listPostAtcProduct == null)
            return
        val indexOfPostAtc = getIndexOfInspirationListPostAtc()
        if(indexOfPostAtc > 0) {
            viewUpdater.refreshItemAtIndex(indexOfListATCSelectedProduct+1, listPostAtcProduct)
        } else {
            showListPostAtcProduct(
                listATCSelectedProduct ,
                listPostAtcProduct,
            )
        }
    }

    private fun getIndexOfInspirationListPostAtc() : Int {
        return viewUpdater.itemList?.indexOfFirst{
            it is InspirationListPostAtcDataView
        }.orZero()
    }

    private fun showListPostAtcProduct(
        listATCSelectedProduct: Visitable<*>?,
        listPostAtcSelectedProduct: Visitable<*>?,
    ) {
        if(listATCSelectedProduct == null || listPostAtcSelectedProduct == null)
            return

        viewUpdater.insertItemAfter(
            listPostAtcSelectedProduct,
            listATCSelectedProduct
        )
    }


    private fun onAddToCartUseCaseSuccess(
        addToCartDataModel: AddToCartDataModel?,
        product: InspirationCarouselDataView.Option.Product,
    ) {
        inspirationListAtcView.updateSearchBarNotification()

        val message = addToCartDataModel?.data?.message?.firstOrNull() ?: ""
        inspirationListAtcView.openAddToCartToaster(message, true)

        val cartId = addToCartDataModel?.data?.cartId ?: ""
        val quantity = addToCartDataModel?.data?.quantity ?: 0

        val trackingData =
            InspirationCarouselTrackingUnificationDataMapper.createCarouselTrackingUnificationData(
                product,
                getSearchParameter(),
                cartId,
                quantity
            )
        inspirationListAtcView.trackItemClick(trackingData)
        inspirationListAtcView.trackAddToCart(trackingData, product)
        if (product.isOrganicAds) inspirationListAtcView.trackAdsClick(product)
    }

    private fun onAddToCartUseCaseFailed(throwable: Throwable?) {
        val message = throwable?.message ?: ""
        inspirationListAtcView.openAddToCartToaster(message, false)
    }

    private fun executeAtcCommon(
        product: InspirationCarouselDataView.Option.Product,
        type: String,
    ) {
        val requestParams = product.createAddToCartRequestParams()

        addToCartUseCase.setParams(requestParams)
        addToCartUseCase.execute(
            {
                onAddToCartUseCaseSuccess(it, product)
                getPostAtcCarousel(product, type)
            },
            ::onAddToCartUseCaseFailed
        )
    }

    private fun InspirationCarouselDataView.Option.Product.createAddToCartRequestParams(): AddToCartRequestParams {
        return AddToCartRequestParams(
            productId = id,
            shopId = shopId,
            quantity = minOrder.toIntOrZero(),
            productName = name,
            price = priceStr,
            userId = if (userSession.isLoggedIn) userSession.userId else DEFAULT_USER_ID
        )
    }

    fun convertInspirationCarouselToInspirationListAtc(
        data: InspirationCarouselDataView
    ): List<Visitable<*>> {
        return data.options.map {
            InspirationListAtcDataView(
                option = it,
                type = data.type,
            )
        }
    }

    override fun setVisibilityInspirationCarouselPostAtcOnVisitableList(
        visibility: Boolean,
        item: InspirationListPostAtcDataView,
    ) {
        val indexOfPostAtc = getIndexOfInspirationListPostAtc()
        if(indexOfPostAtc > 0) {
            viewUpdater.refreshItemAtIndex(
                indexOfPostAtc,
                item.copy(isVisible = visibility),
            )
        }
    }
}
