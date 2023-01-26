package com.tokopedia.tokopedianow.test.common.productcard.utils

import android.view.View
import androidx.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import com.tokopedia.kotlin.extensions.view.EMPTY
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
import com.tokopedia.tokopedianow.test.utils.ViewMatchersUtil.isDisplayedWithText
import com.tokopedia.tokopedianow.test.utils.ViewMatchersUtil.isNotDisplayed
import com.tokopedia.tokopedianow.test.utils.ViewMatchersUtil.isTokoNowProductCardNameTypographyDisplayed
import com.tokopedia.tokopedianow.test.utils.ViewMatchersUtil.isTokoNowProductCardProgressBarDisplayed
import com.tokopedia.tokopedianow.test.utils.ViewMatchersUtil.isTokoNowQuantityEditorViewDisplayed
import com.tokopedia.tokopedianow.test.utils.ViewMatchersUtil.isTokoNowWishlistButtonDisplayedMatcher
import org.hamcrest.Matcher

internal object TokoNowProductCardModelMatcherData {
    private const val PRODUCT_IMAGE_URL = "https://images.tokopedia.net/img/cache/200-square/product-1/2019/12/29/234900908/234900908_33fe7619-52b3-4d5d-9bc9-672549dea45b_1728_1728.jpg"

    fun getProductCardModelMatcherData(): List<TokoNowProductCardMatcherModel> {
        return listOf(
            getProduct(),
            getOosProductCard(),
            getOosProductCardWithWishlistAndSimilarProduct(),
            getFlashSaleProductWithGreyTextColor(),
            getFlashSaleProductWithRedTextColor(),
            getNormalProductWithAssignedValueBestSeller(),
            getNormalProductWithAssignedValueNewProduct(),
            getNormalProductWithPriceLabel()
        )
    }

    private fun getProduct(
        labelGroupList: List<LabelGroup> = emptyList(),
        discount: String = "25%",
        slashPrice: String = "Rp500.000",
        minOrder: Int = 1,
        maxOrder: Int = 10,
        availableStock: Int = 100,
        orderQuantity: Int = 0,
        price: String = "Rp1.500.000",
        name: String = "Strawberry Impor",
        needToShowQuantityEditor: Boolean = false,
        usePreDraw: Boolean = true,
        rating: String = "4.5",
        additionalMatchers: Map<Int, Matcher<View>> = emptyMap(),
        progressBarLabel: String = "",
        progressBarPercentage: Int = 0,
        hasBeenWishlist:Boolean = false,
        isWishlistShown: Boolean = false,
        isSimilarProductShown: Boolean = false
    ): TokoNowProductCardMatcherModel {
        val productCardModel = TokoNowProductCardViewUiModel(
            imageUrl = PRODUCT_IMAGE_URL,
            minOrder = minOrder,
            maxOrder = maxOrder,
            availableStock = availableStock,
            orderQuantity = orderQuantity,
            price = price,
            discount = discount,
            slashPrice = slashPrice,
            name = name,
            rating = rating,
            needToShowQuantityEditor = needToShowQuantityEditor,
            usePreDraw = usePreDraw,
            labelGroupList = labelGroupList,
            progressBarLabel = progressBarLabel,
            progressBarPercentage = progressBarPercentage,
            hasBeenWishlist = hasBeenWishlist,
            isWishlistShown = isWishlistShown,
            isSimilarProductShown = isSimilarProductShown
        )

        val productCardMatcher = mapOf(
            Pair(
                first = R.id.quantity_editor,
                second = if (needToShowQuantityEditor) {
                    isTokoNowQuantityEditorViewDisplayed(
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
                } else {
                    if (labelGroupList.getLabelGroupPriceTitle().isNotBlank()) {
                        isDisplayedWithText(labelGroupList.getLabelGroupPriceTitle())
                    } else {
                        isNotDisplayed()
                    }
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
                second = isTokoNowProductCardNameTypographyDisplayed(
                    productName = productCardModel.name,
                    needToChangeMaxLinesName = productCardModel.needToChangeMaxLinesName
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
                second = if (progressBarLabel.isNotBlank()) isTokoNowProductCardProgressBarDisplayed(progressBarPercentage) else isNotDisplayed()
            ),
            Pair(
                first = R.id.progress_typography,
                second = if (progressBarLabel.isNotBlank()) isDisplayedWithText(progressBarLabel) else isNotDisplayed()
            ),
            Pair(
                first = R.id.wishlist_button,
                second = if (isWishlistShown) isTokoNowWishlistButtonDisplayedMatcher(hasBeenWishlist) else isNotDisplayed()
            ),
            Pair(
                first = R.id.similar_product_typography,
                second = if (isSimilarProductShown) isDisplayed() else isNotDisplayed()
            )
        )

        return TokoNowProductCardMatcherModel(productCardModel, productCardMatcher + additionalMatchers)
    }

    /**
     * Normal Product
     */

    private fun getNormalProductWithAssignedValueBestSeller(): TokoNowProductCardMatcherModel {
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
            )
        )
    }

    private fun getNormalProductWithAssignedValueNewProduct(): TokoNowProductCardMatcherModel {
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
            )
        )
    }

    private fun getNormalProductWithPriceLabel(): TokoNowProductCardMatcherModel {
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
            )
        )
    }

    /**
     * Flash Sale Product
     */

    private fun getFlashSaleProductWithGreyTextColor() = getProduct(
        needToShowQuantityEditor = true,
        progressBarLabel = "Terjual Sebagian",
        progressBarPercentage = 10
    )

    private fun getFlashSaleProductWithRedTextColor() = getProduct(
        needToShowQuantityEditor = true,
        progressBarLabel = "Segera Habis",
        progressBarPercentage = 80
    )

    /**
     * Out Of Stock (OOS) Product
     */

    private fun getOosProductCard() : TokoNowProductCardMatcherModel {
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
            labelGroupList = labelGroupList
        )
    }

    private fun getOosProductCardWithWishlistAndSimilarProduct() : TokoNowProductCardMatcherModel {
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
            labelGroupList = labelGroupList
        )
    }

    private fun List<LabelGroup>.getLabelGroupPriceTitle(): String = find { it.isPricePosition() }?.title.orEmpty()

    private fun List<LabelGroup>.getLabelGroupAssignedValueTitle(): String = find { it.isNewProductLabelPosition() || it.isBestSellerPosition() }?.title.orEmpty()

    private fun List<LabelGroup>.getLabelGroupWeightTitle(): String = find { it.isWeightPosition() }?.title.orEmpty()
}
