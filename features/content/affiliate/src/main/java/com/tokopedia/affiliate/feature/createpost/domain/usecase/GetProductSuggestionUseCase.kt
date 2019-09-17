package com.tokopedia.affiliate.feature.createpost.domain.usecase

import com.tokopedia.affiliate.feature.createpost.data.pojo.productsuggestion.affiliate.AffiliateProductItem
import com.tokopedia.affiliate.feature.createpost.view.viewmodel.ProductSuggestionItem
import com.tokopedia.affiliate.feature.createpost.view.viewmodel.RelatedProductItem
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

/**
 * @author by milhamj on 2019-09-17.
 */
class GetProductSuggestionUseCase @Inject constructor(
        private val getAffiliateProductSuggestionUseCase: GetAffiliateProductSuggestionUseCase,
        private val getShopProductSuggestionUseCase: GetShopProductSuggestionUseCase
) : UseCase<List<ProductSuggestionItem>>() {

    var params: HashMap<String, Any> = hashMapOf()
    var isAffiliateType = true

    override suspend fun executeOnBackground(): List<ProductSuggestionItem> {
        return if (isAffiliateType)  {
            getAffiliateProductSuggestionUseCase.executeOnBackground().map {
                ProductSuggestionItem(
                        it.productId.toString(),
                        it.adId.toString(),
                        it.title,
                        it.commissionValueDisplay,
                        it.image,
                        ProductSuggestionItem.TYPE_AFFILIATE
                )
            }
        } else {
            getShopProductSuggestionUseCase.params = params
            getShopProductSuggestionUseCase.executeOnBackground().map {
                ProductSuggestionItem(
                        it.id,
                        "",
                        it.name,
                        it.price,
                        it.imageUri,
                        ProductSuggestionItem.TYPE_SHOP
                )
            }
        }
    }

    fun cancelAllJobs() {
        getShopProductSuggestionUseCase.cancelJobs()
        getAffiliateProductSuggestionUseCase.cancelJobs()
        cancelJobs()
    }
}