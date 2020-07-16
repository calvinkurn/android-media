package com.tokopedia.product.addedit.imagepicker.view

import com.tokopedia.product.addedit.imagepicker.domain.interactor.ClearCacheCatalogUseCase
import com.tokopedia.product.addedit.imagepicker.domain.interactor.GetCatalogImageUseCase
import com.tokopedia.product.addedit.imagepicker.view.model.CatalogModelView
import com.tokopedia.product.addedit.imagepicker.view.presenter.ImagePickerCatalogContract
import com.tokopedia.product.addedit.imagepicker.view.presenter.ImagePickerCatalogPresenter
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import rx.Subscriber

class ImagePickerCatalogPresenterTest {

    @RelaxedMockK
    lateinit var getCatalogImageUseCase: GetCatalogImageUseCase

    @RelaxedMockK
    lateinit var clearCacheCatalogUseCase: ClearCacheCatalogUseCase

    @RelaxedMockK
    private lateinit var spyView: ImagePickerCatalogContract.View

    private val presenter: ImagePickerCatalogPresenter by lazy {
        spyk(ImagePickerCatalogPresenter(getCatalogImageUseCase, clearCacheCatalogUseCase))
    }

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        presenter.attachView(spyView)
    }

    @After
    fun cleanUp() {
        presenter.detachView()
    }

    @Test
    fun `should success getCatalogImage`() {
        val result = listOf<CatalogModelView>()
        every {
            getCatalogImageUseCase.execute(any(), any())
        } answers {
            secondArg<Subscriber<List<CatalogModelView>>>().onCompleted()
            secondArg<Subscriber<List<CatalogModelView>>>().onNext(result)
        }

        presenter.getCatalogImage("")

        verify {
            spyView.renderList(result)
        }
    }

    @Test
    fun `should failed getCatalogImage`() {
        val exception = mockk<Exception>()
        every {
            getCatalogImageUseCase.execute(any(), any())
        } answers {
            secondArg<Subscriber<List<CatalogModelView>>>().onCompleted()
            secondArg<Subscriber<List<CatalogModelView>>>().onError(exception)
        }

        presenter.getCatalogImage("")

        verify {
            spyView.showGetListError(exception)
        }
    }

    @Test
    fun `just run clearCacheCatalog`() {
        presenter.clearCacheCatalog()
        Assert.assertTrue(true)
    }
}