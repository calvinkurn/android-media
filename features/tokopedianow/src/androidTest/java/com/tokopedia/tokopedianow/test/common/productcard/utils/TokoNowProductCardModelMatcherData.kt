package com.tokopedia.tokopedianow.test.common.productcard.utils

import android.view.View
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.tokopedianow.common.model.LABEL_BEST_SELLER
import com.tokopedia.tokopedianow.common.model.LABEL_GIMMICK
import com.tokopedia.tokopedianow.common.model.LABEL_PRICE
import com.tokopedia.tokopedianow.common.model.LABEL_STATUS
import com.tokopedia.tokopedianow.common.model.LABEL_WEIGHT
import com.tokopedia.tokopedianow.common.model.LIGHT_GREEN
import com.tokopedia.tokopedianow.common.model.TEXT_DARK_ORANGE
import com.tokopedia.tokopedianow.common.model.TRANSPARENT_BLACK
import com.tokopedia.tokopedianow.common.model.TokoNowProductCardViewUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowProductCardViewUiModel.LabelGroup
import com.tokopedia.tokopedianow.test.R
import com.tokopedia.tokopedianow.test.common.productcard.model.TokoNowProductCardMatcherModel
import com.tokopedia.tokopedianow.test.common.productcard.utils.ViewMatchersUtil.isDisplayedWithText
import com.tokopedia.tokopedianow.test.common.productcard.utils.ViewMatchersUtil.isNotDisplayed
import com.tokopedia.tokopedianow.test.common.productcard.utils.ViewMatchersUtil.isProductNameTypographyDisplayed
import com.tokopedia.tokopedianow.test.common.productcard.utils.ViewMatchersUtil.isProgressBarDisplayed
import com.tokopedia.tokopedianow.test.common.productcard.utils.ViewMatchersUtil.isQuantityEditorDisplayed
import com.tokopedia.tokopedianow.test.common.productcard.utils.ViewMatchersUtil.isWishlistButtonDisplayed
import org.hamcrest.Matcher

internal object TokoNowProductCardModelMatcherData {
    private const val PRODUCT_IMAGE_URL = "https://images.tokopedia.net/img/cache/200-square/product-1/2019/12/29/234900908/234900908_33fe7619-52b3-4d5d-9bc9-672549dea45b_1728_1728.jpg"

    fun getProductCardModelMatcherData(isCarousel: Boolean): List<TokoNowProductCardMatcherModel> {
        return listOf(
            getProduct(isCarousel = isCarousel),
            getProductWithDiscountIntType(isCarousel = isCarousel),
            getProductWithAssignedValueBestSeller(isCarousel = isCarousel),
            getProductWithAssignedValueNewProduct(isCarousel = isCarousel),
            getProductWithPriceLabel(isCarousel = isCarousel),
            getOosProductCard(isCarousel = isCarousel),
            getOosProductCardWithWishlistAndSimilarProduct(isCarousel = isCarousel),
            getFlashSaleProductWithGreyTextColor(isCarousel = isCarousel),
            getFlashSaleProductWithRedTextColor(isCarousel = isCarousel)
        )
    }

    private fun getProduct(
        labelGroupList: List<LabelGroup> = emptyList(),
        discount: String = "25%",
        discountInt: Int = 0,
        slashPrice: String = "Rp500.000",
        minOrder: Int = 1,
        maxOrder: Int = 10,
        availableStock: Int = 100,
        orderQuantity: Int = 0,
        price: String = "Rp1.500.000",
        name: String = "Strawberry Impor",
        needToShowQuantityEditor: Boolean = false,
        rating: String = "4.5",
        additionalMatchers: Map<Int, Matcher<View?>> = emptyMap(),
        progressBarLabel: String = "",
        progressBarPercentage: Int = 0,
        hasBeenWishlist:Boolean = false,
        isWishlistShown: Boolean = false,
        isSimilarProductShown: Boolean = false,
        isCarousel: Boolean
    ): TokoNowProductCardMatcherModel {
        val productCardModel = TokoNowProductCardViewUiModel(
            imageUrl = PRODUCT_IMAGE_URL,
            minOrder = minOrder,
            maxOrder = maxOrder,
            availableStock = availableStock,
            orderQuantity = orderQuantity,
            price = price,
            discount = discount,
            discountInt = discountInt,
            slashPrice = slashPrice,
            name = name,
            rating = rating,
            needToShowQuantityEditor = needToShowQuantityEditor,
            usePreDraw = isCarousel,
            labelGroupList = labelGroupList,
            progressBarLabel = progressBarLabel,
            progressBarPercentage = progressBarPercentage,
            hasBeenWishlist = hasBeenWishlist,
            isWishlistShown = isWishlistShown,
            isSimilarProductShown = isSimilarProductShown,
            needToChangeMaxLinesName = !isCarousel
        )

        val matchers = mapOf(
            Pair(
                first = R.id.quantity_editor,
                second = if (needToShowQuantityEditor) {
                    isQuantityEditorDisplayed(
                        minOrder = productCardModel.minOrder,
                        maxOrder = productCardModel.maxOrder,
                        orderQuantity = productCardModel.orderQuantity
                    )
                } else {
                    isNotDisplayed()
                }
            ),
            Pair(
                first = R.id.image_filter_view,
                second = isDisplayed()
            ),
            Pair(
                first = R.id.promo_label,
                second = if (productCardModel.discount.isNotBlank()) {
                    isDisplayedWithText(productCardModel.discount)
                } else if (productCardModel.discountInt.isMoreThanZero()) {
                    isDisplayedWithText("${productCardModel.discountInt}%")
                } else if (labelGroupList.getLabelGroupPriceTitle().isNotBlank()) {
                    isDisplayedWithText(labelGroupList.getLabelGroupPriceTitle())
                } else {
                    isNotDisplayed()
                }
            ),
            Pair(
                first = R.id.slash_price_typography,
                second = if (productCardModel.slashPrice.isNotBlank()) isDisplayedWithText(productCardModel.slashPrice) else isNotDisplayed()
            ),
            Pair(
                first = R.id.main_price_typography,
                second = isDisplayedWithText(productCardModel.price)
            ),
            Pair(
                first = R.id.product_name_typography,
                second = isProductNameTypographyDisplayed(
                    productName = productCardModel.name,
                    needToChangeMaxLinesName = productCardModel.needToChangeMaxLinesName,
                    promoLabelAvailable = productCardModel.discount.isNotBlank() || productCardModel.discountInt.isMoreThanZero() || labelGroupList.getLabelGroupPriceTitle().isNotBlank()
                )
            ),
            Pair(
                first = R.id.rating_icon,
                second = if (productCardModel.isFlashSale()) isNotDisplayed() else isDisplayed()
            ),
            Pair(
                first = R.id.rating_typography,
                second = if (productCardModel.isFlashSale()) isNotDisplayed() else isDisplayedWithText(productCardModel.rating)
            ),
            Pair(
                first = R.id.progress_bar,
                second = if (progressBarLabel.isNotBlank()) isProgressBarDisplayed(progressBarPercentage) else isNotDisplayed()
            ),
            Pair(
                first = R.id.progress_typography,
                second = if (progressBarLabel.isNotBlank()) isDisplayedWithText(progressBarLabel) else isNotDisplayed()
            ),
            Pair(
                first = R.id.wishlist_button,
                second = if (isWishlistShown) isWishlistButtonDisplayed(hasBeenWishlist) else isNotDisplayed()
            ),
            Pair(
                first = R.id.similar_product_typography,
                second = if (isSimilarProductShown) isDisplayed() else isNotDisplayed()
            )
        )

        return TokoNowProductCardMatcherModel(
            model = productCardModel,
            matchers = matchers + additionalMatchers
        )
    }

    private fun getProductWithDiscountIntType(isCarousel: Boolean): TokoNowProductCardMatcherModel = getProduct(
        isCarousel = isCarousel,
        discountInt = 10,
        discount = String.EMPTY
    )

    private fun getProductWithAssignedValueBestSeller(isCarousel: Boolean): TokoNowProductCardMatcherModel {
        val labelGroupList = listOf(
            LabelGroup(
                position = LABEL_BEST_SELLER,
                type = "#32a852",
                title = "Terlaris"
            ),
            LabelGroup(
                position = LABEL_WEIGHT,
                type = "",
                title = "500gr"
            )
        )

        return getProduct(
            needToShowQuantityEditor = true,
            labelGroupList = labelGroupList,
            additionalMatchers = mapOf(
                Pair(
                    first = R.id.assigned_value_typography,
                    second = isDisplayedWithText(labelGroupList.getLabelGroupAssignedValueTitle())
                ),
                Pair(
                    first = R.id.category_info_typography,
                    second = isDisplayedWithText(labelGroupList.getLabelGroupWeightTitle())
                )
            ),
            isCarousel = isCarousel
        )
    }

    private fun getProductWithAssignedValueNewProduct(isCarousel: Boolean): TokoNowProductCardMatcherModel {
        val labelGroupList = listOf(
            LabelGroup(
                position = LABEL_GIMMICK,
                type = TEXT_DARK_ORANGE,
                title = "Terbaru!"
            ),
            LabelGroup(
                position = LABEL_WEIGHT,
                type = "",
                title = "500gr"
            )
        )

        return getProduct(
            needToShowQuantityEditor = true,
            labelGroupList = labelGroupList,
            additionalMatchers = mapOf(
                Pair(
                    first = R.id.assigned_value_typography,
                    second = isDisplayedWithText(labelGroupList.getLabelGroupAssignedValueTitle())
                ),
                Pair(
                    first = R.id.category_info_typography,
                    second = isDisplayedWithText(labelGroupList.getLabelGroupWeightTitle())
                )
            ),
            isCarousel = isCarousel
        )
    }

    private fun getProductWithPriceLabel(isCarousel: Boolean): TokoNowProductCardMatcherModel {
        val labelGroupList = listOf(
            LabelGroup(
                position = LABEL_PRICE,
                type = LIGHT_GREEN,
                title = "Diskon Bundling"
            ),
            LabelGroup(
                position = LABEL_WEIGHT,
                type = "",
                title = "500gr"
            )
        )

        return getProduct(
            discount = String.EMPTY,
            slashPrice = String.EMPTY,
            labelGroupList = labelGroupList,
            additionalMatchers = mapOf(
                Pair(
                    first = R.id.category_info_typography,
                    second = isDisplayedWithText(labelGroupList.getLabelGroupWeightTitle())
                )
            ),
            isCarousel = isCarousel
        )
    }

    /**
     * Flash Sale Product
     */

    private fun getFlashSaleProductWithGreyTextColor(isCarousel: Boolean) = getProduct(
        needToShowQuantityEditor = true,
        progressBarLabel = "Terjual Sebagian",
        progressBarPercentage = 10,
        isCarousel = isCarousel
    )

    private fun getFlashSaleProductWithRedTextColor(isCarousel: Boolean) = getProduct(
        needToShowQuantityEditor = true,
        progressBarLabel = "Segera Habis",
        progressBarPercentage = 80,
        isCarousel = isCarousel
    )

    /**
     * Out Of Stock (OOS) Product
     */

    private fun getOosProductCard(isCarousel: Boolean) : TokoNowProductCardMatcherModel {
        val labelGroupList = listOf(
            LabelGroup(
                position = LABEL_STATUS,
                type = TRANSPARENT_BLACK,
                title = "Stok Habis"
            )
        )

        return getProduct(
            discount = String.EMPTY,
            slashPrice = String.EMPTY,
            minOrder = 10,
            availableStock = 2,
            labelGroupList = labelGroupList,
            isCarousel = isCarousel
        )
    }

    private fun getOosProductCardWithWishlistAndSimilarProduct(isCarousel: Boolean) : TokoNowProductCardMatcherModel {
        val labelGroupList = listOf(
            LabelGroup(
                position = LABEL_STATUS,
                type = TRANSPARENT_BLACK,
                title = "Stok Habis"
            )
        )

        return getProduct(
            discount = String.EMPTY,
            slashPrice = String.EMPTY,
            minOrder = 10,
            availableStock = 2,
            isWishlistShown = true,
            isSimilarProductShown = true,
            labelGroupList = labelGroupList,
            isCarousel = isCarousel
        )
    }

    private fun List<LabelGroup>.getLabelGroupPriceTitle(): String = find { it.isPricePosition() }?.title.orEmpty()

    private fun List<LabelGroup>.getLabelGroupAssignedValueTitle(): String = find { it.isNewProductLabelPosition() || it.isBestSellerPosition() }?.title.orEmpty()

    private fun List<LabelGroup>.getLabelGroupWeightTitle(): String = find { it.isWeightPosition() }?.title.orEmpty()
}
