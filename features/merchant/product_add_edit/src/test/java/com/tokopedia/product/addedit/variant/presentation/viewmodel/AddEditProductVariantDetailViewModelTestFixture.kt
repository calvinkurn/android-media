package com.tokopedia.product.addedit.variant.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.product.addedit.common.util.ResourceProvider
import com.tokopedia.product.addedit.detail.presentation.model.DetailInputModel
import com.tokopedia.product.addedit.preview.presentation.model.ProductInputModel
import com.tokopedia.product.addedit.variant.presentation.model.*
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.MockKAnnotations
import io.mockk.clearAllMocks
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.spyk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import org.junit.Before
import org.junit.Rule
import org.junit.jupiter.api.AfterEach
import kotlin.jvm.Throws

@ExperimentalCoroutinesApi
abstract class AddEditProductVariantDetailViewModelTestFixture {

    @get:Rule
    val instantTaskExcecutorRule = InstantTaskExecutorRule()

    @RelaxedMockK
    lateinit var resourceProvider: ResourceProvider

    @RelaxedMockK
    lateinit var userSession: UserSessionInterface

    private val testCoroutineDispatcher = TestCoroutineDispatcher()
    val productInputModel: ProductInputModel by lazy {
        ProductInputModel(
                detailInputModel= DetailInputModel(
                        productName="lemari arsip besi",
                        categoryName="Dapur - Peralatan-Makan-Minum - Sumpit",
                        categoryId="969",
                        catalogId="0",
                        price=9999.toBigInteger(),
                        stock=1, minOrder=1,
                        condition="NEW", sku="", status=1),
                variantInputModel= VariantInputModel(
                        products= listOf(
                            ProductVariantInputModel(combination= listOf(0, 0), price=9999.toBigInteger(), status="ACTIVE", stock=1, isPrimary=false),
                            ProductVariantInputModel(combination= listOf(0, 1), price=9999.toBigInteger(), status="ACTIVE", stock=1, isPrimary=false),
                            ProductVariantInputModel(combination= listOf(1, 0), price=9999.toBigInteger(), status="ACTIVE", stock=1, isPrimary=false),
                            ProductVariantInputModel(combination= listOf(1, 1), price=9999.toBigInteger(), status="ACTIVE", stock=1, isPrimary=false)),
                        selections= listOf(
                            SelectionInputModel(variantId="1", variantName="Warna", unitID="0", identifier="colour", options= listOf(
                                OptionInputModel(unitValueID="9", value="Merah"),
                                OptionInputModel(unitValueID="6", value="Biru Muda"))),
                            SelectionInputModel(variantId="29", variantName="Ukuran", unitID="27", unitName="Default", identifier="size", options= listOf(
                                OptionInputModel(unitValueID="449", value="8"),
                                OptionInputModel(unitValueID="450", value="10")))),
                        sizecharts= PictureVariantInputModel(),
                        isRemoteDataHasVariant=true),
                productId=1221443121)
    }

    protected val viewModel: AddEditProductVariantDetailViewModel by lazy {
        spyk(AddEditProductVariantDetailViewModel(
                resourceProvider,
                userSession,
                testCoroutineDispatcher
        ))
    }

    @Before
    @Throws(Exception::class)
    fun setup() {
        MockKAnnotations.init(this)
    }

    @AfterEach
    fun tearDown() {
        clearAllMocks()
        Dispatchers.resetMain()
    }
}