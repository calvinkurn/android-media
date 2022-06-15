package com.tokopedia.vouchercreation.product.preview

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.vouchercreation.product.create.data.response.ProductId
import com.tokopedia.vouchercreation.product.create.domain.entity.*
import com.tokopedia.vouchercreation.product.create.domain.usecase.GetCouponFacadeUseCase
import com.tokopedia.vouchercreation.product.create.domain.usecase.InitiateCouponUseCase
import com.tokopedia.vouchercreation.product.create.domain.usecase.create.CreateCouponFacadeUseCase
import com.tokopedia.vouchercreation.product.create.domain.usecase.update.UpdateCouponFacadeUseCase
import com.tokopedia.vouchercreation.product.list.view.model.ProductUiModel
import com.tokopedia.vouchercreation.product.list.view.model.VariantUiModel
import com.tokopedia.vouchercreation.shop.create.view.uimodel.initiation.InitiateVoucherUiModel
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.*

class CouponPreviewViewModelTest {

    @RelaxedMockK
    lateinit var initiateCouponUseCase: InitiateCouponUseCase

    @RelaxedMockK
    lateinit var createCouponUseCase: CreateCouponFacadeUseCase

    @RelaxedMockK
    lateinit var updateCouponUseCase: UpdateCouponFacadeUseCase

    @RelaxedMockK
    lateinit var getCouponDetailUseCase: GetCouponFacadeUseCase

    @RelaxedMockK
    lateinit var areInputValidObserver: Observer<in Boolean>

    @RelaxedMockK
    lateinit var createCouponObserver: Observer<in Result<Int>>

    @RelaxedMockK
    lateinit var updateCouponResultObserver: Observer<in Result<Boolean>>

    @RelaxedMockK
    lateinit var couponDetailObserver: Observer<in Result<CouponWithMetadata>>

    @RelaxedMockK
    lateinit var couponCreationEligibilityObserver: Observer<in Result<Int>>

    @get:Rule
    val rule = InstantTaskExecutorRule()


    private val viewModel by lazy {
        CouponPreviewViewModel(
            CoroutineTestDispatchersProvider,
            initiateCouponUseCase, createCouponUseCase, updateCouponUseCase, getCouponDetailUseCase
        )
    }

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel.areInputValid.observeForever(areInputValidObserver)
        viewModel.createCoupon.observeForever(createCouponObserver)
        viewModel.updateCouponResult.observeForever(updateCouponResultObserver)
        viewModel.couponDetail.observeForever(couponDetailObserver)
        viewModel.couponCreationEligibility.observeForever(couponCreationEligibilityObserver)
    }

    @After
    fun tearDown() {
        viewModel.areInputValid.removeObserver(areInputValidObserver)
        viewModel.createCoupon.removeObserver(createCouponObserver)
        viewModel.updateCouponResult.removeObserver(updateCouponResultObserver)
        viewModel.couponDetail.removeObserver(couponDetailObserver)
        viewModel.couponCreationEligibility.removeObserver(couponCreationEligibilityObserver)
    }

    @Test
    fun `When create coupon success, should emit success to observer`() = runBlocking {
        //Given
        val isCreateMode = true
        val couponInformation = mockk<CouponInformation>()
        val couponSetting = mockk<CouponSettings>()
        val couponProduct = mockk<CouponProduct>()
        val couponProducts = listOf(couponProduct)
        val parentProductIds = listOf<Long>(2111)

        val createdCouponId = 29347923

        coEvery {
            createCouponUseCase.execute(
                isCreateMode,
                couponInformation,
                couponSetting,
                couponProducts,
                parentProductIds
            )
        } returns createdCouponId

        //When
        viewModel.createCoupon(
            isCreateMode,
            couponInformation,
            couponSetting,
            couponProducts,
            parentProductIds
        )

        //Then
        coVerify { createCouponObserver.onChanged(Success(createdCouponId)) }
    }


    @Test
    fun `When create coupon error, should emit error to observer`() = runBlocking {
        //Given
        val error = MessageErrorException()
        val isCreateMode = true
        val couponInformation = mockk<CouponInformation>()
        val couponSetting = mockk<CouponSettings>()
        val couponProduct = mockk<CouponProduct>()
        val couponProducts = listOf(couponProduct)
        val parentProductIds = listOf<Long>(2111)

        coEvery {
            createCouponUseCase.execute(
                isCreateMode,
                couponInformation,
                couponSetting,
                couponProducts,
                parentProductIds
            )
        } throws error

        //When
        viewModel.createCoupon(
            isCreateMode,
            couponInformation,
            couponSetting,
            couponProducts,
            parentProductIds
        )

        //Then
        coVerify { createCouponObserver.onChanged(Fail(error)) }
    }

    @Test
    fun `When update coupon success, should emit success to observer`() = runBlocking {
        //Given
        val isUpdateSuccess = true
        val couponInformation = mockk<CouponInformation>()
        val couponSetting = mockk<CouponSettings>()
        val couponProduct = mockk<CouponProduct>()
        val couponProducts = listOf(couponProduct)
        val parentProductIds = listOf<Long>(2111)

        val couponId: Long = 29347923

        coEvery {
            updateCouponUseCase.execute(
                couponId,
                couponInformation,
                couponSetting,
                couponProducts,
                parentProductIds
            )
        } returns isUpdateSuccess

        //When
        viewModel.updateCoupon(
            couponId,
            couponInformation,
            couponSetting,
            couponProducts,
            parentProductIds
        )

        //Then
        coVerify { updateCouponResultObserver.onChanged(Success(isUpdateSuccess)) }
    }

    @Test
    fun `When update coupon false, should emit error to observer`() = runBlocking {
        //Given
        val isUpdateSuccess = false
        val couponInformation = mockk<CouponInformation>()
        val couponSetting = mockk<CouponSettings>()
        val couponProduct = mockk<CouponProduct>()
        val couponProducts = listOf(couponProduct)
        val parentProductIds = listOf<Long>(2111)

        val couponId: Long = 29347923

        coEvery {
            updateCouponUseCase.execute(
                couponId,
                couponInformation,
                couponSetting,
                couponProducts,
                parentProductIds
            )
        } returns isUpdateSuccess

        //When
        viewModel.updateCoupon(
            couponId,
            couponInformation,
            couponSetting,
            couponProducts,
            parentProductIds
        )

        //Then
        coVerify { updateCouponResultObserver.onChanged(Success(isUpdateSuccess)) }
    }

    @Test
    fun `When update coupon error, should emit error to observer`() = runBlocking {
        //Given
        val error = MessageErrorException()
        val couponInformation = mockk<CouponInformation>()
        val couponSetting = mockk<CouponSettings>()
        val couponProduct = mockk<CouponProduct>()
        val couponProducts = listOf(couponProduct)
        val parentProductIds = listOf<Long>(2111)

        val couponId: Long = 29347923

        coEvery {
            updateCouponUseCase.execute(
                couponId,
                couponInformation,
                couponSetting,
                couponProducts,
                parentProductIds
            )
        } throws error

        //When
        viewModel.updateCoupon(
            couponId,
            couponInformation,
            couponSetting,
            couponProducts,
            parentProductIds
        )

        //Then
        coVerify { updateCouponResultObserver.onChanged(Fail(error)) }
    }

    @Test
    fun `When get coupon detail success on create mode, should emit success to observer`() =
        runBlocking {
            //Given
            val expected = mockk<CouponWithMetadata>()
            val couponId: Long = 29347923
            val pageMode = CouponPreviewFragment.Mode.CREATE
            val shouldCreateNewCoupon = true

            coEvery {
                getCouponDetailUseCase.execute(
                    couponId,
                    shouldCreateNewCoupon
                )
            } returns expected

            //When
            viewModel.getCouponDetail(couponId, pageMode)

            //Then
            coVerify { couponDetailObserver.onChanged(Success(expected)) }
        }

    @Test
    fun `When get coupon detail and is on create mode, isToCreateNewCoupon property should be true`() =
        runBlocking {
            //Given
            val expected = mockk<CouponWithMetadata>()
            val couponId: Long = 29347923
            val pageMode = CouponPreviewFragment.Mode.CREATE
            val shouldCreateNewCoupon = true

            coEvery {
                getCouponDetailUseCase.execute(
                    couponId,
                    shouldCreateNewCoupon
                )
            } returns expected

            //When
            viewModel.getCouponDetail(couponId, pageMode)

            //Then
            coVerify {
                getCouponDetailUseCase.execute(
                    couponId,
                    shouldCreateNewCoupon
                )
            }
            coVerify { couponDetailObserver.onChanged(Success(expected)) }
        }

    @Test
    fun `When get coupon detail and is on duplicate mode, isToCreateNewCoupon property should be true`() =
        runBlocking {
            //Given
            val expected = mockk<CouponWithMetadata>()
            val couponId: Long = 29347923
            val pageMode = CouponPreviewFragment.Mode.DUPLICATE
            val shouldCreateNewCoupon = true

            coEvery {
                getCouponDetailUseCase.execute(
                    couponId,
                    shouldCreateNewCoupon
                )
            } returns expected

            //When
            viewModel.getCouponDetail(couponId, pageMode)

            //Then
            coVerify {
                getCouponDetailUseCase.execute(
                    couponId,
                    shouldCreateNewCoupon
                )
            }
            coVerify { couponDetailObserver.onChanged(Success(expected)) }
        }

    @Test
    fun `When get coupon detail success on update mode, should emit success to observer`() =
        runBlocking {
            //Given
            val expected = mockk<CouponWithMetadata>()
            val couponId: Long = 29347923
            val pageMode = CouponPreviewFragment.Mode.UPDATE
            val shouldCreateNewCoupon = false

            coEvery {
                getCouponDetailUseCase.execute(
                    couponId,
                    shouldCreateNewCoupon
                )
            } returns expected

            //When
            viewModel.getCouponDetail(couponId, pageMode)

            //Then
            coVerify { couponDetailObserver.onChanged(Success(expected)) }
        }


    @Test
    fun `When get coupon detail error, should emit error to observer`() = runBlocking {
        //Given
        val error = MessageErrorException()
        val couponId: Long = 29347923
        val pageMode = CouponPreviewFragment.Mode.CREATE
        val shouldCreateNewCoupon = true

        coEvery {
            getCouponDetailUseCase.execute(
                couponId,
                shouldCreateNewCoupon
            )
        } throws error

        //When
        viewModel.getCouponDetail(couponId, pageMode)

        //Then
        coVerify { couponDetailObserver.onChanged(Fail(error)) }
    }

    @Test
    fun `When check coupon eligibility success, should emit success to observer`() = runBlocking {
        //Given
        val response = InitiateVoucherUiModel()

        coEvery { initiateCouponUseCase.executeOnBackground() } returns response

        //When
        viewModel.checkCouponCreationEligibility()

        //Then
        assert(viewModel.couponCreationEligibility.value is Success)
    }

    @Test
    fun `When check coupon eligibility error, should emit success to observer`() = runBlocking {
        //Given
        val error = MessageErrorException()

        coEvery { initiateCouponUseCase.executeOnBackground() } throws error

        //When
        viewModel.checkCouponCreationEligibility()

        //Then
        coVerify { couponCreationEligibilityObserver.onChanged(Fail(error)) }
    }


    @Test
    fun `When mode is update mode, should return true`() = runBlocking {
        //Given
        val expected = true
        val mode = CouponPreviewFragment.Mode.UPDATE

        //When
        val actual = viewModel.isUpdateMode(mode)

        //Then
        assertEquals(expected, actual)
    }

    @Test
    fun `When mode is not update mode, should return false`() = runBlocking {
        //Given
        val expected = false
        val mode = CouponPreviewFragment.Mode.DUPLICATE

        //When
        val actual = viewModel.isUpdateMode(mode)

        //Then
        assertEquals(expected, actual)
    }

    @Test
    fun `When mode is duplicate mode, should return true`() = runBlocking {
        //Given
        val expected = true
        val mode = CouponPreviewFragment.Mode.DUPLICATE

        //When
        val actual = viewModel.isDuplicateMode(mode)

        //Then
        assertEquals(expected, actual)
    }

    @Test
    fun `When mode is not duplicate mode, should return false`() = runBlocking {
        //Given
        val expected = false
        val mode = CouponPreviewFragment.Mode.CREATE

        //When
        val actual = viewModel.isDuplicateMode(mode)

        //Then
        assertEquals(expected, actual)
    }

    @Test
    fun `When coupon information target is not selected, should return false`() = runBlocking {
        //Given
        val couponInformation = buildCouponInformation().copy(target = CouponInformation.Target.NOT_SELECTED)

        val expected = false

        //When
        val actual = viewModel.isCouponInformationValid(couponInformation)

        //Then
        assertEquals(expected, actual)
    }

    @Test
    fun `When coupon information target is private and coupon code is empty, should return false`() =
        runBlocking {
            //Given
            val couponInformation = buildCouponInformation().copy(target = CouponInformation.Target.PRIVATE, code = "")

            val expected = false

            //When
            val actual = viewModel.isCouponInformationValid(couponInformation)

            //Then
            assertEquals(expected, actual)
        }

    @Test
    fun `When coupon information target is private and coupon code is not empty, should return true`() =
        runBlocking {
            //Given
            val couponInformation = buildCouponInformation().copy(target = CouponInformation.Target.PRIVATE)

            val expected = true

            //When
            val actual = viewModel.isCouponInformationValid(couponInformation)

            //Then
            assertEquals(expected, actual)
        }

    @Test
    fun `When coupon information target is public and coupon code is empty, should return true`() =
        runBlocking {
            //Given
            val couponInformation = buildCouponInformation().copy(code = "")

            val expected = true

            //When
            val actual = viewModel.isCouponInformationValid(couponInformation)

            //Then
            assertEquals(expected, actual)
        }

    @Test
    fun `When coupon information name is empty, should return false`() = runBlocking {
        //Given
        val couponInformation = buildCouponInformation().copy(name = "")

        val expected = false

        //When
        val actual = viewModel.isCouponInformationValid(couponInformation)

        //Then
        assertEquals(expected, actual)
    }

    @Test
    fun `When coupon information fields are valid, should return true`() = runBlocking {
        //Given
        val couponInformation = buildCouponInformation()

        val expected = true

        //When
        val actual = viewModel.isCouponInformationValid(couponInformation)

        //Then
        assertEquals(expected, actual)
    }

    @Test
    fun `When get warehouse id, should return warehouse id correctly`() {
        val expected = "WR001"

        viewModel.selectedWarehouseId = expected

        val actual = viewModel.selectedWarehouseId

        assertEquals(expected, actual)
    }

    @Test
    fun `When coupon setting is null, observer should receive false`() {
        val expected = false

        val mode = CouponPreviewFragment.Mode.CREATE
        val couponInformation = buildCouponInformation()
        val couponSettings = null
        val couponProducts = listOf<CouponProduct>()

        viewModel.validateCoupon(mode, couponInformation, couponSettings, couponProducts)

        verify { areInputValidObserver.onChanged(expected) }
    }


    @Test
    fun `When coupon information is null, observer should receive false`() {
        val expected = false

        val mode = CouponPreviewFragment.Mode.CREATE
        val couponInformation = null
        val couponSettings = buildCouponSettings()
        val couponProducts = listOf<CouponProduct>()

        viewModel.validateCoupon(mode, couponInformation, couponSettings, couponProducts)

        verify { areInputValidObserver.onChanged(expected) }
    }


    @Test
    fun `When coupon products is empty, observer should receive false`() {
        val expected = false

        val mode = CouponPreviewFragment.Mode.CREATE
        val couponInformation = buildCouponInformation()
        val couponSettings = buildCouponSettings()
        val couponProducts = listOf<CouponProduct>()

        viewModel.validateCoupon(mode, couponInformation, couponSettings, couponProducts)

        verify { areInputValidObserver.onChanged(expected) }
    }


    @Test
    fun `When update mode and products is not empty, observer should receive true`() {
        val expected = true

        val mode = CouponPreviewFragment.Mode.UPDATE
        val couponInformation = buildCouponInformation()
        val couponSettings = buildCouponSettings()
        val couponProducts = listOf(CouponProduct("", "", 0))

        viewModel.validateCoupon(mode, couponInformation, couponSettings, couponProducts)

        verify { areInputValidObserver.onChanged(expected) }
    }

    @Test
    fun `When update mode and products is empty, observer should receive false`() {
        val expected = false

        val mode = CouponPreviewFragment.Mode.UPDATE
        val couponInformation = buildCouponInformation()
        val couponSettings = buildCouponSettings()
        val couponProducts = listOf<CouponProduct>()

        viewModel.validateCoupon(mode, couponInformation, couponSettings, couponProducts)

        verify { areInputValidObserver.onChanged(expected) }
    }

    @Test
    fun `When duplicate mode and products is not empty, observer should receive true`() {
        val expected = true

        val mode = CouponPreviewFragment.Mode.DUPLICATE
        val couponInformation = buildCouponInformation()
        val couponSettings = buildCouponSettings()
        val couponProducts = listOf(CouponProduct("", "", 0))

        viewModel.validateCoupon(mode, couponInformation, couponSettings, couponProducts)

        verify { areInputValidObserver.onChanged(expected) }
    }


    @Test
    fun `When duplicate mode and products is empty, observer should receive false`() {
        val expected = false

        val mode = CouponPreviewFragment.Mode.DUPLICATE
        val couponInformation = buildCouponInformation()
        val couponSettings = buildCouponSettings()
        val couponProducts = listOf<CouponProduct>()

        viewModel.validateCoupon(mode, couponInformation, couponSettings, couponProducts)

        verify { areInputValidObserver.onChanged(expected) }
    }

    @Test
    fun `When all input is valid, observer should receive true`() {
        val expected = true

        val mode = CouponPreviewFragment.Mode.CREATE
        val couponInformation = buildCouponInformation()
        val couponSettings = buildCouponSettings()
        val couponProducts = listOf(CouponProduct("", "", 0))

        viewModel.validateCoupon(mode, couponInformation, couponSettings, couponProducts)

        verify { areInputValidObserver.onChanged(expected) }
    }

    @Test
    fun `When user modify selected products, should return product id from modified products `() {
        val modifiedSelectedProductIds = mutableListOf(
            ProductUiModel(id = "232"),
            ProductUiModel(id = "234"),
            ProductUiModel(id = "235"),
        )

        val nonModifiedSelectedProductIds = mutableListOf(
            ProductId(parentProductId = 232),
            ProductId(parentProductId = 234),
            ProductId(parentProductId = 235)
        )

        val productIds = listOf<Long>(232, 234, 235)

        val actual = viewModel.getParentProductIds(modifiedSelectedProductIds, nonModifiedSelectedProductIds)

        assertEquals(productIds, actual)
    }

    @Test
    fun `When user not modify selected products, should return product id from existing selected products `() {
        val modifiedSelectedProductIds = mutableListOf<ProductUiModel>()

        val nonModifiedSelectedProductIds = mutableListOf(
            ProductId(parentProductId = 400),
            ProductId(parentProductId = 401),
            ProductId(parentProductId = 402)
        )

        val productIds = listOf<Long>(400, 401, 402)

        val actual = viewModel.getParentProductIds(modifiedSelectedProductIds, nonModifiedSelectedProductIds)

        assertEquals(productIds, actual)
    }

    @Test
    fun `When map selected product, should map data correctly`() {

        val selectedProductIds = mutableListOf(
            ProductId(parentProductId = 400,childProductId = listOf(4001)),
            ProductId(parentProductId = 401, childProductId = listOf(4011)),
            ProductId(parentProductId = 402, childProductId = listOf(4012))
        )

        val expected = listOf(
            ProductUiModel(id = "400", variants = listOf(VariantUiModel(isSelected = true, variantId = "4001"))),
            ProductUiModel(id = "401", variants = listOf(VariantUiModel(isSelected = true, variantId = "4011"))),
            ProductUiModel(id = "402", variants = listOf(VariantUiModel(isSelected = true, variantId = "4012")))
        )


        val actual = viewModel.mapSelectedProductIdsToProductUiModels(
            selectedProductIds
        )

        assertEquals(expected, actual)
    }

    @Test
    fun `When map selected product to coupon product and parent product selected, should map data correctly`() {

        val selectedProduct = listOf(
            ProductUiModel(id = "400",  sold = 100, isSelected = true),
            ProductUiModel(id = "401", sold = 200,  isSelected = true),
            ProductUiModel(id = "402", sold = 300,  isSelected = true)
        )

        val expected = listOf(
            CouponProduct("400", "", 100),
            CouponProduct("401", "", 200),
            CouponProduct("402", "", 300),
        )


        val actual = viewModel.mapSelectedProductsToCouponProductData(selectedProduct)

        assertEquals(expected, actual)
    }

    @Test
    fun `When map selected product and both of parent product and variant selected, should map data correctly`() {

        val selectedProduct = listOf(
            ProductUiModel(id = "400", sold = 100, isSelected = true, variants = listOf(VariantUiModel(isSelected = true, variantId = "4001"))),
            ProductUiModel(id = "401",  sold = 200, isSelected = true, variants = listOf(VariantUiModel(isSelected = true, variantId = "4011"))),
            ProductUiModel(id = "402",  sold = 300, isSelected = true, variants = listOf(VariantUiModel(isSelected = true, variantId = "4012")))
        )

        val expected = listOf(
            CouponProduct("4001", "", 100),
            CouponProduct("4011", "", 200),
            CouponProduct("4012", "", 300),
        )


        val actual = viewModel.mapSelectedProductsToCouponProductData(selectedProduct)

        assertEquals(expected, actual)
    }

    @Test
    fun `When map selected product and parent product selected but variant is not selected, should map data correctly`() {

        val selectedProduct = listOf(
            ProductUiModel(id = "400", sold = 100, isSelected = true, variants = listOf(VariantUiModel(isSelected = false, variantId = "4001"))),
            ProductUiModel(id = "401",  sold = 200, isSelected = true, variants = listOf(VariantUiModel(isSelected = false, variantId = "4011"))),
            ProductUiModel(id = "402",  sold = 300, isSelected = true, variants = listOf(VariantUiModel(isSelected = false, variantId = "4012")))
        )

        val expected = listOf(
            CouponProduct("400", "", 100),
            CouponProduct("401", "", 200),
            CouponProduct("402", "", 300),
        )


        val actual = viewModel.mapSelectedProductsToCouponProductData(selectedProduct)

        assertEquals(expected, actual)
    }

    @Test
    fun `When map selected product to coupon product and parent product is not selected, should return empty list`() {

        val selectedProduct = listOf(
            ProductUiModel(id = "400", isSelected = false),
            ProductUiModel(id = "401", isSelected = false),
            ProductUiModel(id = "402", isSelected = false)
        )

        val expected = listOf<CouponProduct>()


        val actual = viewModel.mapSelectedProductsToCouponProductData(selectedProduct)

        assertEquals(expected, actual)
    }

    private fun buildCouponInformation(): CouponInformation {
        return CouponInformation(
            CouponInformation.Target.PUBLIC,
            "Voucher25RB",
            "VCR25RB",
            CouponInformation.Period(
                Date(), Date()
            )
        )
    }

    private fun buildCouponSettings(): CouponSettings {
        return CouponSettings(
            CouponType.CASHBACK,
            DiscountType.PERCENTAGE,
            MinimumPurchaseType.NOMINAL,
            0,
            50,
            25000,
            10,
            50000,
            500000
        )
    }
}