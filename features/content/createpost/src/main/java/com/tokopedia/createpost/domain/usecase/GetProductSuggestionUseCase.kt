package com.tokopedia.createpost.domain.usecase

import com.tokopedia.createpost.view.viewmodel.ProductSuggestionItem
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
    var type: String = ""

    override suspend fun executeOnBackground(): List<ProductSuggestionItem> {
        return when (type) {
            ProductSuggestionItem.TYPE_AFFILIATE -> {
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
            }
            ProductSuggestionItem.TYPE_SHOP -> {
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
            else -> throw IllegalStateException("Unsupported type: $type")
        }
    }

    fun cancelAllJobs() {
        getShopProductSuggestionUseCase.cancelJobs()
        getAffiliateProductSuggestionUseCase.cancelJobs()
        cancelJobs()
    }
}