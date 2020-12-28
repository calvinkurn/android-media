package com.tokopedia.productcard

import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.productcard.ProductCardModel.LabelGroupVariant
import com.tokopedia.productcard.utils.LIGHT_GREY
import com.tokopedia.productcard.utils.TYPE_VARIANT_COLOR
import com.tokopedia.productcard.utils.TYPE_VARIANT_CUSTOM
import com.tokopedia.productcard.utils.TYPE_VARIANT_SIZE
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

internal class ProductCardModelTest {

    private val labelVariantColor1 = LabelGroupVariant(typeVariant = TYPE_VARIANT_COLOR, hexColor = "#ffffff")
    private val labelVariantColor2 = LabelGroupVariant(typeVariant = TYPE_VARIANT_COLOR, hexColor = "#000000")
    private val labelVariantColor3 = LabelGroupVariant(typeVariant = TYPE_VARIANT_COLOR, hexColor = "#bf00ff")
    private val labelVariantColor4 = LabelGroupVariant(typeVariant = TYPE_VARIANT_COLOR, hexColor = "#ff0016")
    private val labelVariantColor5 = LabelGroupVariant(typeVariant = TYPE_VARIANT_COLOR, hexColor = "#121212")
    private val labelVariantColor6 = LabelGroupVariant(typeVariant = TYPE_VARIANT_COLOR, hexColor = "#12225E")

    private val labelVariantSize1 = LabelGroupVariant(typeVariant = TYPE_VARIANT_SIZE, title = "S", type = LIGHT_GREY)
    private val labelVariantSize2 = LabelGroupVariant(typeVariant = TYPE_VARIANT_SIZE, title = "M", type = LIGHT_GREY)
    private val labelVariantSize3 = LabelGroupVariant(typeVariant = TYPE_VARIANT_SIZE, title = "L", type = LIGHT_GREY)
    private val labelVariantSize4 = LabelGroupVariant(typeVariant = TYPE_VARIANT_SIZE, title = "XL", type = LIGHT_GREY)
    private val labelVariantSize5 = LabelGroupVariant(typeVariant = TYPE_VARIANT_SIZE, title = "XXL", type = LIGHT_GREY)
    private val labelVariantSize6 = LabelGroupVariant(typeVariant = TYPE_VARIANT_SIZE, title = "XXXL", type = LIGHT_GREY)
    private val labelVariantSize7 = LabelGroupVariant(typeVariant = TYPE_VARIANT_SIZE, title = "All Size", type = LIGHT_GREY)
    private val labelVariantSize8 = LabelGroupVariant(typeVariant = TYPE_VARIANT_SIZE, title = "All Size longtxt", type = LIGHT_GREY)

    private val labelVariantCustom = LabelGroupVariant(typeVariant = TYPE_VARIANT_CUSTOM, title = "1")

    private fun `Test rendered label group variant`(
            givenLabelGroupVariant: List<LabelGroupVariant>,
            expectedRenderedLabelGroupVariant: List<LabelGroupVariant>
    ) {
        val productCardModel = ProductCardModel(labelGroupVariantList = givenLabelGroupVariant)
        val actualRenderedLabelGroupVariant = productCardModel.getRenderedLabelGroupVariantList()

        assertThat(actualRenderedLabelGroupVariant, `is`(expectedRenderedLabelGroupVariant))
    }

    @Test
    fun `getRenderedLabelGroupVariant should return from labelGroupVariant`() {
        val labelGroupVariant = listOf(labelVariantColor1, labelVariantColor2, labelVariantColor3)

        `Test rendered label group variant`(labelGroupVariant, labelGroupVariant)
    }

    @Test
    fun `getRenderedLabelGroupVariant should not contain less than 2 labels for color variant`() {
        val labelGroupVariant = listOf(labelVariantColor1, labelVariantCustom)

        `Test rendered label group variant`(labelGroupVariant, listOf())
    }

    @Test
    fun `getRenderedLabelGroupVariant should not contain more than 5 labels for color variant`() {
        val labelGroupVariant = listOf(
                labelVariantColor1,
                labelVariantColor2,
                labelVariantColor3,
                labelVariantColor4,
                labelVariantColor5,
                labelVariantColor6,
                labelVariantCustom
        )

        val expectedRenderedLabelGroupVariant = listOf(
                labelVariantColor1,
                labelVariantColor2,
                labelVariantColor3,
                labelVariantColor4,
                labelVariantColor5,
                labelVariantCustom
        )

        `Test rendered label group variant`(labelGroupVariant, expectedRenderedLabelGroupVariant)
    }

    @Test
    fun `getRenderedLabelGroupVariant should not contain less than 2 labels for size variant`() {
        val labelGroupVariant = listOf(labelVariantSize1, labelVariantCustom)

        `Test rendered label group variant`(labelGroupVariant, listOf())
    }

    @Test
    fun `getRenderedLabelGroupVariant should not contain more than 5 labels for size variant`() {
        val labelGroupVariant = listOf(
                labelVariantSize1,
                labelVariantSize2,
                labelVariantSize3,
                labelVariantSize4,
                labelVariantSize5,
                labelVariantSize6,
                labelVariantCustom
        )

        val expectedRenderedLabelGroupVariant = listOf(
                labelVariantSize1,
                labelVariantSize2,
                labelVariantSize3,
                labelVariantSize4,
                labelVariantSize5,
                labelVariantCustom
        )

        `Test rendered label group variant`(labelGroupVariant, expectedRenderedLabelGroupVariant)
    }

    @Test
    fun `getRenderedLabelGroupVariant should prioritize color variant`() {
        val labelGroupVariant = listOf(
                labelVariantColor1,
                labelVariantColor2,
                labelVariantSize1,
                labelVariantSize2,
                labelVariantSize3,
                labelVariantCustom
        )

        val expectedRenderedLabelGroupVariant = listOf(
                labelVariantColor1,
                labelVariantColor2,
                labelVariantCustom
        )

        `Test rendered label group variant`(labelGroupVariant, expectedRenderedLabelGroupVariant)
    }

    @Test
    fun `getRenderedLabelGroupVariant should show size variant if color variant is less than 2`() {
        val labelGroupVariant = listOf(
                labelVariantColor1,
                labelVariantSize1,
                labelVariantSize2,
                labelVariantSize3,
                labelVariantCustom
        )

        val expectedRenderedLabelGroupVariant = listOf(
                labelVariantSize1,
                labelVariantSize2,
                labelVariantSize3,
                labelVariantCustom
        )

        `Test rendered label group variant`(labelGroupVariant, expectedRenderedLabelGroupVariant)
    }

    @Test
    fun `getRenderedLabelGroupVariant should show size variant only until maximum characters allowed`() {
        val labelGroupVariant = listOf(
                labelVariantSize1,
                labelVariantSize2,
                labelVariantSize3,
                labelVariantSize4,
                labelVariantSize7,
                labelVariantCustom
        )

        val expectedRenderedLabelGroupVariant = listOf(
                labelVariantSize1,
                labelVariantSize2,
                labelVariantSize3,
                labelVariantSize4,
                LabelGroupVariant(typeVariant = TYPE_VARIANT_CUSTOM, title = (labelVariantCustom.title.toIntOrZero() + 1).toString())
        )

        `Test rendered label group variant`(labelGroupVariant, expectedRenderedLabelGroupVariant)
    }

    @Test
    fun `getRenderedLabelGroupVariant should create new custom variant when there are hidden labels`() {
        val labelGroupVariant = listOf(
                labelVariantSize1,
                labelVariantSize2,
                labelVariantSize3,
                labelVariantSize4,
                labelVariantSize7
        )

        val expectedRenderedLabelGroupVariant = listOf(
                labelVariantSize1,
                labelVariantSize2,
                labelVariantSize3,
                labelVariantSize4,
                LabelGroupVariant(typeVariant = TYPE_VARIANT_CUSTOM, title = 1.toString())
        )

        `Test rendered label group variant`(labelGroupVariant, expectedRenderedLabelGroupVariant)
    }

    @Test
    fun `getRenderedLabelGroupVariant should handle custom variant with non-integer title`() {
        val labelGroupVariant = listOf(
                labelVariantSize1,
                labelVariantSize2,
                labelVariantSize3,
                labelVariantSize4,
                labelVariantSize7,
                LabelGroupVariant(typeVariant = TYPE_VARIANT_CUSTOM, title = "notinteger")
        )

        val expectedRenderedLabelGroupVariant = listOf(
                labelVariantSize1,
                labelVariantSize2,
                labelVariantSize3,
                labelVariantSize4,
                LabelGroupVariant(typeVariant = TYPE_VARIANT_CUSTOM, title = 1.toString())
        )

        `Test rendered label group variant`(labelGroupVariant, expectedRenderedLabelGroupVariant)
    }
}