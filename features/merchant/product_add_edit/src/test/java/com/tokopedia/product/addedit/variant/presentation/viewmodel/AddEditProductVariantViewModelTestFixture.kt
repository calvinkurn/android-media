package com.tokopedia.product.addedit.variant.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import com.tokopedia.product.addedit.variant.data.model.Unit
import com.tokopedia.product.addedit.variant.data.model.UnitValue
import com.tokopedia.product.addedit.variant.data.model.VariantDetail
import com.tokopedia.product.addedit.variant.domain.GetVariantCategoryCombinationUseCase
import io.mockk.MockKAnnotations
import io.mockk.clearAllMocks
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.spyk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.jupiter.api.AfterEach
import kotlin.jvm.Throws

@ExperimentalCoroutinesApi
abstract class AddEditProductVariantViewModelTestFixture {

    @get:Rule
    val instantTaskExcecutorRule = InstantTaskExecutorRule()

    @RelaxedMockK
    lateinit var isInputValidObserver: Observer<Boolean>

    @RelaxedMockK
    lateinit var getVariantCategoryCombinationUseCase: GetVariantCategoryCombinationUseCase

    @Suppress("UNCHECKED_CAST")
    private val mIsInputValid: MediatorLiveData<Boolean> by lazy {
        getPrivateField(viewModel, "mIsInputValid") as MediatorLiveData<Boolean>
    }

    @Suppress("UNCHECKED_CAST")
    val variantDataMap: HashMap<Int, VariantDetail> by lazy {
        getPrivateField(viewModel, "variantDataMap") as HashMap<Int, VariantDetail>
    }

    private val testCoroutineDispatcher = TestCoroutineDispatcher()

    val variantDetailTest1 = VariantDetail(
            variantID=1,
            identifier="",
            name="Ukuran Kemasan",
            status=1,
            units= listOf(
                    Unit(variantUnitID=62,
                            status=1,
                            unitName="Volume",
                            unitShortName="Volume",
                            unitValues= mutableListOf(
                                    UnitValue(variantUnitValueID=816, status=1, value="45 ml"),
                                    UnitValue(variantUnitValueID=817, status=1, value="150 ml"),
                                    UnitValue(variantUnitValueID=818, status=1, value="200 ml"),
                                    UnitValue(variantUnitValueID=819, status=1, value="320 ml"),
                                    UnitValue(variantUnitValueID=820, status=1, value="400 ml"),
                                    UnitValue(variantUnitValueID=821, status=1, value="550 ml"),
                                    UnitValue(variantUnitValueID=822, status=1, value="670 ml"),
                                    UnitValue(variantUnitValueID=823, status=1, value="720 ml"),
                                    UnitValue(variantUnitValueID=824, status=1, value="1 l"),
                                    UnitValue(variantUnitValueID=825, status=1, value="1.5 l"),
                                    UnitValue(variantUnitValueID=826, status=1, value="2 l"),
                                    UnitValue(variantUnitValueID=827, status=1, value="3 l"),
                                    UnitValue(variantUnitValueID=828, status=1, value="4 l"),
                                    UnitValue(variantUnitValueID=829, status=1, value="5 L")
                            )
                    ),
                    Unit(variantUnitID=63,
                            status=1,
                            unitName="Berat",
                            unitShortName="Berat",
                            unitValues= mutableListOf(
                                    UnitValue(variantUnitValueID=830, status=1, value="25 gram"),
                                    UnitValue(variantUnitValueID=831, status=1, value="40 gram"),
                                    UnitValue(variantUnitValueID=832, status=1, value="100 gram"),
                                    UnitValue(variantUnitValueID=833, status=1, value="160 gram"),
                                    UnitValue(variantUnitValueID=834, status=1, value="190 gram"),
                                    UnitValue(variantUnitValueID=835, status=1, value="225 gram"),
                                    UnitValue(variantUnitValueID=836, status=1, value="300 gram"),
                                    UnitValue(variantUnitValueID=837, status=1, value="400 gram"),
                                    UnitValue(variantUnitValueID=838, status=1, value="500 gram"),
                                    UnitValue(variantUnitValueID=839, status=1, value="1/4 kg"),
                                    UnitValue(variantUnitValueID=840, status=1, value="1/2 kg"),
                                    UnitValue(variantUnitValueID=841, status=1, value="3/4 kg"),
                                    UnitValue(variantUnitValueID=842, status=1, value="1 kg"),
                                    UnitValue(variantUnitValueID=843, status=1, value="2 kg"),
                                    UnitValue(variantUnitValueID=844, status=1, value="2.5 kg"),
                                    UnitValue(variantUnitValueID=845, status=1, value="3 kg"),
                                    UnitValue(variantUnitValueID=846, status=1, value="4 kg"),
                                    UnitValue(variantUnitValueID=847, status=1, value="5 Kg")
                            )
                    ))
    )

    val variantDetailTest2 = VariantDetail(
            variantID=29,
            identifier="size",
            name="Ukuran",
            status=1,
            units= listOf(
                    Unit(variantUnitID=27,
                            status=1,
                            unitName="Default",
                            unitShortName="default",
                            unitValues= mutableListOf(
                                    UnitValue(variantUnitValueID=445, status=1, value="0"),
                                    UnitValue(variantUnitValueID=446, status=1, value="2"),
                                    UnitValue(variantUnitValueID=447, status=1, value="4"),
                                    UnitValue(variantUnitValueID=448, status=1, value="6"),
                                    UnitValue(variantUnitValueID=449, status=1, value="8"),
                                    UnitValue(variantUnitValueID=450, status=1, value="10"),
                                    UnitValue(variantUnitValueID=451, status=1, value="12"),
                                    UnitValue(variantUnitValueID=452, status=1, value="14"),
                                    UnitValue(variantUnitValueID=455, status=1, value="S"),
                                    UnitValue(variantUnitValueID=453, status=1, value="16"),
                                    UnitValue(variantUnitValueID=454, status=1, value="XS"),
                                    UnitValue(variantUnitValueID=456, status=1, value="M"),
                                    UnitValue(variantUnitValueID=457, status=1, value="L"),
                                    UnitValue(variantUnitValueID=458, status=1, value="XL"),
                                    UnitValue(variantUnitValueID=459, status=1, value="XXL"),
                                    UnitValue(variantUnitValueID=460, status=1, value="All")
                            )
                    )
            )
    )

    val variantDetailsTest = listOf(variantDetailTest1, variantDetailTest2)

    protected val spiedViewModel: AddEditProductVariantViewModel by lazy {
        spyk(AddEditProductVariantViewModel(
                testCoroutineDispatcher,
                getVariantCategoryCombinationUseCase
        ))
    }

    protected val viewModel: AddEditProductVariantViewModel by lazy {
        AddEditProductVariantViewModel(
                testCoroutineDispatcher,
                getVariantCategoryCombinationUseCase)
    }

    @Before
    @Throws(Exception::class)
    fun setup() {
        MockKAnnotations.init(this)
        mIsInputValid.observeForever(isInputValidObserver)
    }

    @AfterEach
    fun tearDown() {
        clearAllMocks()
        Dispatchers.resetMain()
    }

    @After
    fun cleanUp() {
        mIsInputValid.removeObserver(isInputValidObserver)
    }

    private fun getPrivateField(owner: Any, name: String): Any? {
        return owner::class.java.getDeclaredField(name).let {
            it.isAccessible = true
            return@let it.get(owner)
        }
    }
}