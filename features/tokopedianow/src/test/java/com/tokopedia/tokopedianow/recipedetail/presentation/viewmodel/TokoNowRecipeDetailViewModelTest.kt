package com.tokopedia.tokopedianow.recipedetail.presentation.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.localizationchooseaddress.domain.response.GetStateChosenAddressResponse
import com.tokopedia.minicart.common.domain.data.MiniCartItem
import com.tokopedia.minicart.common.domain.data.MiniCartItemKey
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.tokopedianow.common.model.MediaItemUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowServerErrorUiModel
import com.tokopedia.tokopedianow.recipebookmark.domain.model.AddRecipeBookmarkResponse
import com.tokopedia.tokopedianow.recipebookmark.domain.model.RemoveRecipeBookmarkResponse
import com.tokopedia.tokopedianow.recipebookmark.persentation.uimodel.TagUiModel
import com.tokopedia.tokopedianow.recipedetail.constant.MediaType
import com.tokopedia.tokopedianow.recipedetail.domain.model.TokoNowGetRecipe
import com.tokopedia.tokopedianow.recipedetail.presentation.uimodel.BookmarkUiModel
import com.tokopedia.tokopedianow.recipedetail.presentation.uimodel.IngredientTabUiModel
import com.tokopedia.tokopedianow.recipedetail.presentation.uimodel.IngredientUiModel
import com.tokopedia.tokopedianow.recipedetail.presentation.uimodel.InstructionTabUiModel
import com.tokopedia.tokopedianow.recipedetail.presentation.uimodel.InstructionUiModel
import com.tokopedia.tokopedianow.recipedetail.presentation.uimodel.MediaSliderUiModel
import com.tokopedia.tokopedianow.recipedetail.presentation.uimodel.OutOfCoverageUiModel
import com.tokopedia.tokopedianow.recipedetail.presentation.uimodel.RecipeDetailLoadingUiModel
import com.tokopedia.tokopedianow.recipedetail.presentation.uimodel.RecipeInfoUiModel
import com.tokopedia.tokopedianow.recipedetail.presentation.uimodel.RecipeProductUiModel
import com.tokopedia.tokopedianow.recipedetail.presentation.uimodel.RecipeTabUiModel
import com.tokopedia.tokopedianow.recipedetail.presentation.uimodel.SectionTitleUiModel.IngredientSectionTitle
import com.tokopedia.tokopedianow.recipedetail.presentation.uimodel.SectionTitleUiModel.InstructionSectionTitle
import com.tokopedia.tokopedianow.searchcategory.jsonToObject
import com.tokopedia.unit.test.ext.verifyValueEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Test

@ExperimentalCoroutinesApi
class TokoNowRecipeDetailViewModelTest : TokoNowRecipeDetailViewModelTestFixture() {

    @Test
    fun `given invalid shopId when checkAddressData should call get address use case`() {
        val firstShopId = 0L // invalid shopId
        val secondShopId = 1L
        val warehouseId = "5"
        val recipeId = "3"
        val slug = "recipe-1"

        val addressResponse = GetStateChosenAddressResponse()
        val getRecipeResponse = "recipedetail/get_recipe_response.json"
            .jsonToObject<TokoNowGetRecipe>()
        val recipeResponse = getRecipeResponse.response.data

        onGetShopId_thenReturn(firstShopId, secondShopId)
        onGetWarehouseId_thenReturn(warehouseId)
        onGetAddressData_thenReturn(addressResponse)
        onGetRecipe_thenReturn(response = recipeResponse)

        viewModel.setRecipeData(recipeId, slug)
        viewModel.checkAddressData()

        verifyGetAddressDataUseCaseCalled()
        verifyGetRecipeUseCaseCalled(
            recipeId = recipeId,
            slug = slug,
            warehouseId = warehouseId
        )
    }

    @Test
    fun `given get address error when checkAddressData should remove loading and add server error to layout list`() {
        val shopId = 0L

        onGetShopId_thenReturn(shopId)
        onGetAddressData_thenReturn(NullPointerException())

        viewModel.checkAddressData()

        verifyGetAddressDataUseCaseCalled()

        val expectedLayoutList = listOf(TokoNowServerErrorUiModel)

        viewModel.layoutList
            .verifyValueEquals(expectedLayoutList)
    }

    @Test
    fun `given valid shopId when checkAddressData should call get recipe use case and map layout list`() {
        val shopId = 3L
        val warehouseId = "3"
        val isBookmarked = true
        val outOfCoverage = false
        val instruction = "\u003cp\u003etest 123\u003c/p\u003e\n"

        val recipeId = "2"
        val slug = "recipe-3"
        val title = "Test Create Recipe UAM"

        val getRecipeResponse = "recipedetail/get_recipe_response.json"
            .jsonToObject<TokoNowGetRecipe>()
        val recipeResponse = getRecipeResponse.response.data

        val miniCartKey = MiniCartItemKey(id = "2148241523")
        val miniCart = MiniCartItem.MiniCartItemProduct(productId = "2148241523", quantity = 5)
        val miniCartData = MiniCartSimplifiedData(miniCartItems = mapOf(miniCartKey to miniCart))

        onGetShopId_thenReturn(shopId)
        onGetWarehouseId_thenReturn(warehouseId)
        onGetIsOutOfCoverage_thenReturn(outOfCoverage)
        onGetRecipe_thenReturn(response = recipeResponse)

        viewModel.setMiniCartData(miniCartData)
        viewModel.setRecipeData(recipeId, slug)
        viewModel.checkAddressData()

        val mediaItems = listOf(
            MediaItemUiModel(
                url = "https://vod-staging.tokopedia.com/view/adaptive.m3u8?id=3be5201de4ef417087f8aaf7d15bfa7b",
                thumbnailUrl = "https://vod-staging.tokopedia.com/view/adaptive.m3u8?id=3be5201de4ef417087f8aaf7d15bfa7b",
                type = MediaType.VIDEO,
                position = 1
            ),
            MediaItemUiModel(
                url = "https://images-staging.tokopedia.net/img/DqYeeh/2022/8/25/31ee9868-090d-46e9-848d-40486c639198.png",
                thumbnailUrl = "https://images-staging.tokopedia.net/img/DqYeeh/2022/8/25/31ee9868-090d-46e9-848d-40486c639198.png",
                type = MediaType.IMAGE,
                position = 2
            )
        )

        val mediaSlider = MediaSliderUiModel(mediaItems)

        val tags = listOf(
            TagUiModel(
                tag = "Test Create Tag 3",
                shouldFormatTag = false
            ),
            TagUiModel(
                tag = "Test Create Tag 2",
                shouldFormatTag = false
            ),
            TagUiModel(
                tag = "Test Create Tag 1",
                shouldFormatTag = false
            ),
            TagUiModel(
                tag = "1",
                shouldFormatTag = true
            )
        )

        val imageUrls = listOf(
            "https://images-staging.tokopedia.net/img/DqYeeh/2022/8/25/31ee9868-090d-46e9-848d-40486c639198.png"
        )

        val recipeInfo = RecipeInfoUiModel(
            id = "20",
            title = title,
            portion = 32,
            duration = 13,
            tags = tags,
            thumbnail = "https://images-staging.tokopedia.net/img/DqYeeh/2022/8/25/31ee9868-090d-46e9-848d-40486c639198.png",
            imageUrls = imageUrls,
            shareUrl = "https://www.tokopedia.com/now/recipe/recipe-3"
        )

        val productList = listOf(
            RecipeProductUiModel(
                id = "2148241523",
                shopId = "480552",
                name = "kaos testing 112",
                quantity = 5,
                stock = 7,
                minOrder = 1,
                maxOrder = 7,
                priceFmt = "Rp150",
                imageUrl = "https://images.tokopedia.net/img/cache/250-square/hDjmkQ/2022/1/17/4524771c-4b31-4eb1-9491-0adb581431b1.jpg",
                slashedPrice = "Rp200",
                discountPercentage = "20",
                similarProducts = emptyList(),
                categoryId = "983",
                categoryName = "Dapur",
                position = 1
            )
        )

        val ingredientTabUiModel = IngredientTabUiModel(productList)

        val ingredients = listOf(
            IngredientUiModel(
                name = "Test Create Ingredient 6",
                quantity = "12",
                unit = "kg",
                isLastItem = true
            )
        )

        val instructionUiModel = InstructionUiModel(instruction)

        val instructionTabItems = mutableListOf<Visitable<*>>().apply {
            add(IngredientSectionTitle)
            addAll(ingredients)
            add(InstructionSectionTitle)
            add(instructionUiModel)
        }

        val instructionTabUiModel = InstructionTabUiModel(instructionTabItems)

        val recipeTabUiModel = RecipeTabUiModel(
            1,
            ingredientTabUiModel,
            instructionTabUiModel
        )

        val expectedLayoutList = listOf(
            mediaSlider,
            recipeInfo,
            recipeTabUiModel
        )

        verifyGetAddressDataUseCaseNotCalled()
        verifyGetRecipeUseCaseCalled(
            recipeId = recipeId,
            slug = slug,
            warehouseId = warehouseId
        )

        viewModel.isBookmarked
            .verifyValueEquals(isBookmarked)

        viewModel.recipeInfo
            .verifyValueEquals(recipeInfo)

        viewModel.layoutList
            .verifyValueEquals(expectedLayoutList)
    }

    @Test
    fun `given out of coverage when checkAddressData should add out of coverage item to layout list`() {
        val shopId = 3L
        val warehouseId = "3"
        val outOfCoverage = true
        val isBookmarked = false
        val instruction = "\u003cp\u003etest 123\u003c/p\u003e\n"

        val recipeId = "2"
        val slug = "recipe-3"
        val title = "Test Create Recipe UAM"
        val imageUrl =
            "https://images-staging.tokopedia.net/img/DqYeeh/2022/8/25/31ee9868-090d-46e9-848d-40486c639198.png"

        val getRecipeResponse = "recipedetail/get_recipe_simple_response.json"
            .jsonToObject<TokoNowGetRecipe>()
        val recipeResponse = getRecipeResponse.response.data.copy(isBookmarked = isBookmarked)

        onGetShopId_thenReturn(shopId)
        onGetWarehouseId_thenReturn(warehouseId)
        onGetIsOutOfCoverage_thenReturn(outOfCoverage)
        onGetRecipe_thenReturn(response = recipeResponse)

        viewModel.setRecipeData(recipeId, slug)
        viewModel.checkAddressData()

        val mediaSlider = MediaSliderUiModel(emptyList())

        val recipeInfo = RecipeInfoUiModel(
            id = "20",
            title = title,
            portion = 32,
            duration = 13,
            tags = emptyList(),
            thumbnail = imageUrl,
            imageUrls = listOf(imageUrl),
            shareUrl = "https://www.tokopedia.com/now/recipe/recipe-3"
        )

        val ingredientTabUiModel = IngredientTabUiModel(listOf(OutOfCoverageUiModel))
        val instructionUiModel = InstructionUiModel(instruction)

        val instructionTabItems = mutableListOf<Visitable<*>>().apply {
            add(IngredientSectionTitle)
            addAll(emptyList())
            add(InstructionSectionTitle)
            add(instructionUiModel)
        }

        val instructionTabUiModel = InstructionTabUiModel(instructionTabItems)

        val recipeTabUiModel = RecipeTabUiModel(
            1,
            ingredientTabUiModel,
            instructionTabUiModel
        )

        val expectedLayoutList = listOf(
            mediaSlider,
            recipeInfo,
            recipeTabUiModel
        )

        verifyGetAddressDataUseCaseNotCalled()
        verifyGetRecipeUseCaseCalled(
            recipeId = recipeId,
            slug = slug,
            warehouseId = warehouseId
        )

        viewModel.isBookmarked
            .verifyValueEquals(isBookmarked)

        viewModel.layoutList
            .verifyValueEquals(expectedLayoutList)
    }

    @Test
    fun `given get recipe error when checkAddressData should remove loading and add server error to layout list`() {
        val shopId = 3L

        onGetShopId_thenReturn(shopId)
        onGetRecipe_thenReturn(NullPointerException())

        viewModel.checkAddressData()

        verifyGetRecipeUseCaseCalled()

        val expectedLayoutList = listOf(TokoNowServerErrorUiModel)

        viewModel.layoutList
            .verifyValueEquals(expectedLayoutList)
    }

    @Test
    fun `given recipe is NOT bookmarked and addBookmark true when onClickBookmarkBtn should call add recipe bookmark`() {
        coroutineTestRule.runBlockingTest {
            val shopId = 3L
            val warehouseId = "3"

            val isBookmarked = false
            val addBookmark = true

            val addBookmarkResponse = "recipebookmark/addrecipebookmarksuccess.json"
                .jsonToObject<AddRecipeBookmarkResponse>()
            val getRecipeResponse = "recipedetail/get_recipe_simple_response.json"
                .jsonToObject<TokoNowGetRecipe>()
            val recipeResponse = getRecipeResponse.response.data.copy(isBookmarked = isBookmarked)

            onGetShopId_thenReturn(shopId)
            onGetWarehouseId_thenReturn(warehouseId)
            onGetRecipe_thenReturn(response = recipeResponse)
            onAddRecipeBookmark_thenReturn(response = addBookmarkResponse)

            viewModel.checkAddressData()
            viewModel.onClickBookmarkBtn(addBookmark)
            viewModel.onClickBookmarkBtn(addBookmark)

            advanceTimeBy(500L)

            val expectedIsBookmarked = true
            val expectedBookmarkData = BookmarkUiModel(
                recipeTitle = "Test Create Recipe UAM",
                isSuccess = true
            )

            verifyAddBookmarkUseCaseCalled(recipeId = "20")

            viewModel.addBookmark
                .verifyValueEquals(expectedBookmarkData)

            viewModel.isBookmarked
                .verifyValueEquals(expectedIsBookmarked)
        }
    }

    @Test
    fun `given addBookmark error when onClickBookmarkBtn should set isBookmarked and isSuccess false`() {
        coroutineTestRule.runBlockingTest {
            val shopId = 3L
            val addBookmark = true

            onGetShopId_thenReturn(shopId)
            onAddRecipeBookmark_thenReturn(NullPointerException())

            viewModel.onClickBookmarkBtn(addBookmark)

            advanceTimeBy(500L)

            val expectedIsBookmarked = false
            val expectedBookmarkData = BookmarkUiModel(
                recipeTitle = "",
                isSuccess = false
            )

            verifyAddBookmarkUseCaseCalled()

            viewModel.addBookmark
                .verifyValueEquals(expectedBookmarkData)

            viewModel.isBookmarked
                .verifyValueEquals(expectedIsBookmarked)
        }
    }

    @Test
    fun `given recipe is bookmarked and addBookmark true when onClickBookmarkBtn should NOT call add recipe bookmark`() {
        coroutineTestRule.runBlockingTest {
            val shopId = 3L
            val warehouseId = "3"

            val isBookmarked = true
            val addBookmark = true

            val getRecipeResponse = "recipedetail/get_recipe_simple_response.json"
                .jsonToObject<TokoNowGetRecipe>()
            val recipeResponse = getRecipeResponse.response.data.copy(isBookmarked = isBookmarked)

            onGetShopId_thenReturn(shopId)
            onGetWarehouseId_thenReturn(warehouseId)
            onGetRecipe_thenReturn(response = recipeResponse)

            viewModel.checkAddressData()
            viewModel.onClickBookmarkBtn(addBookmark)

            advanceTimeBy(500L)

            verifyAddBookmarkUseCaseNotCalled()
        }
    }

    @Test
    fun `given recipe is bookmarked and addBookmark false when onClickBookmarkBtn should call remove recipe bookmark`() {
        coroutineTestRule.runBlockingTest {
            val shopId = 3L
            val warehouseId = "3"

            val isBookmarked = true
            val addBookmark = false

            val removeBookmarkResponse = "recipebookmark/removerecipebookmarksuccess.json"
                .jsonToObject<RemoveRecipeBookmarkResponse>()
            val getRecipeResponse = "recipedetail/get_recipe_simple_response.json"
                .jsonToObject<TokoNowGetRecipe>()
            val recipeResponse = getRecipeResponse.response.data.copy(isBookmarked = isBookmarked)

            onGetShopId_thenReturn(shopId)
            onGetWarehouseId_thenReturn(warehouseId)
            onGetRecipe_thenReturn(response = recipeResponse)
            onRemoveRecipeBookmark_thenReturn(response = removeBookmarkResponse)

            viewModel.checkAddressData()
            viewModel.onClickBookmarkBtn(addBookmark)
            viewModel.onClickBookmarkBtn(addBookmark)

            advanceTimeBy(500L)

            val expectedIsBookmarked = false
            val expectedBookmarkData = BookmarkUiModel(
                recipeTitle = "Test Create Recipe UAM",
                isSuccess = true
            )

            verifyRemoveBookmarkUseCaseCalled(recipeId = "20")

            viewModel.removeBookmark
                .verifyValueEquals(expectedBookmarkData)

            viewModel.isBookmarked
                .verifyValueEquals(expectedIsBookmarked)
        }
    }

    @Test
    fun `given removeBookmark error when onClickBookmarkBtn should set isBookmarked true and isSuccess false`() {
        coroutineTestRule.runBlockingTest {
            val shopId = 3L
            val warehouseId = "3"

            val isBookmarked = true
            val addBookmark = false

            val getRecipeResponse = "recipedetail/get_recipe_simple_response.json"
                .jsonToObject<TokoNowGetRecipe>()
            val recipeResponse = getRecipeResponse.response.data.copy(
                id = "15",
                isBookmarked = isBookmarked
            )

            onGetShopId_thenReturn(shopId)
            onGetWarehouseId_thenReturn(warehouseId)
            onGetRecipe_thenReturn(response = recipeResponse)
            onRemoveRecipeBookmark_thenReturn(NullPointerException())

            viewModel.checkAddressData()
            viewModel.onClickBookmarkBtn(addBookmark)

            advanceTimeBy(500L)

            val expectedIsBookmarked = true
            val expectedBookmarkData = BookmarkUiModel(
                recipeTitle = "Test Create Recipe UAM",
                isSuccess = false
            )

            verifyRemoveBookmarkUseCaseCalled(recipeId = "15")

            viewModel.removeBookmark
                .verifyValueEquals(expectedBookmarkData)

            viewModel.isBookmarked
                .verifyValueEquals(expectedIsBookmarked)
        }
    }

    @Test
    fun `given recipe is NOT bookmarked and addBookmark false when onClickBookmarkBtn should NOT call remove recipe bookmark`() {
        coroutineTestRule.runBlockingTest {
            val shopId = 3L
            val warehouseId = "3"

            val isBookmarked = false
            val addBookmark = false

            val getRecipeResponse = "recipedetail/get_recipe_simple_response.json"
                .jsonToObject<TokoNowGetRecipe>()
            val recipeResponse = getRecipeResponse.response.data.copy(isBookmarked = isBookmarked)

            onGetShopId_thenReturn(shopId)
            onGetWarehouseId_thenReturn(warehouseId)
            onGetRecipe_thenReturn(response = recipeResponse)

            viewModel.checkAddressData()
            viewModel.onClickBookmarkBtn(addBookmark)

            advanceTimeBy(500L)

            verifyRemoveBookmarkUseCaseNotCalled()
        }
    }

    @Test
    fun `when onClickBookmarkBtn should call update address data and add loading to layout list`() {
        viewModel.refreshPage()

        val expectedLayoutList = listOf(
            RecipeDetailLoadingUiModel
        )

        verifyUpdateAddressDataCalled()

        viewModel.layoutList
            .verifyValueEquals(expectedLayoutList)
    }

    @Test
    fun `when layoutItemList error should do nothing`() {
        onGetLayoutItemList_returnNull()

        viewModel.setMiniCartData(MiniCartSimplifiedData())

        viewModel.layoutList
            .verifyValueEquals(null)
    }
}
