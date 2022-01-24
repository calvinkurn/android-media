package com.tokopedia.affiliate.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.gson.Gson
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.affiliate.AFFILIATE_INSTAGRAM_REGEX
import com.tokopedia.affiliate.AFFILIATE_TIKTOK_REGEX
import com.tokopedia.affiliate.AFFILIATE_YT_REGEX
import com.tokopedia.affiliate.adapter.AffiliateAdapterTypeFactory
import com.tokopedia.affiliate.model.pojo.AffiliateHeaderItemData
import com.tokopedia.affiliate.model.pojo.AffiliatePortfolioButtonData
import com.tokopedia.affiliate.model.pojo.AffiliatePortfolioUrlInputData
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateHeaderModel
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliatePortfolioButtonModel
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliatePortfolioUrlModel
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class AffiliatePortfolioViewModelTest{
    private val userSessionInterface: UserSessionInterface = mockk()
    private var aff = spyk(AffiliatePortfolioViewModel(userSessionInterface))

    @get:Rule
    var rule = InstantTaskExecutorRule()

    @Before
    @Throws(Exception::class)
    fun setUp() {

        coEvery { userSessionInterface.name } returns ""

        MockKAnnotations.init(this)
        Dispatchers.setMain(TestCoroutineDispatcher())
    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
        Dispatchers.resetMain()
    }

    /**************************** createListForSm *******************************************/
    @Test
    fun listForSmTest(){
        val itemList : ArrayList<Visitable<AffiliateAdapterTypeFactory>> = ArrayList()
        itemList.add(AffiliateHeaderModel(AffiliateHeaderItemData(userSessionInterface.name,true)))
        itemList.add(AffiliatePortfolioUrlModel(AffiliatePortfolioUrlInputData(3,"instagram","Link Instagram","","Contoh: instagram.com/tokopedia","Link tidak valid.",false,regex = AFFILIATE_INSTAGRAM_REGEX)))
        itemList.add(AffiliatePortfolioUrlModel(AffiliatePortfolioUrlInputData(9,"tiktok","Link Tiktok","","Contoh: tiktok.com/tokopedia","Link tidak valid.",false,regex = AFFILIATE_TIKTOK_REGEX)))
        itemList.add(AffiliatePortfolioUrlModel(AffiliatePortfolioUrlInputData(13,"youtube","Link Youtube","","Contoh: youtube.com/tokopedia","Link tidak valid.",false,regex = AFFILIATE_YT_REGEX)))
        itemList.add(AffiliatePortfolioButtonModel(AffiliatePortfolioButtonData("Tambah Sosial Media", UnifyButton.Type.ALTERNATE,UnifyButton.Variant.GHOST)))

        aff.createDefaultListForSm()
        assertEquals(Gson().toJson(aff.affiliatePortfolioData.value),Gson().toJson(itemList))
        assertEquals(Gson().toJson(aff.getPortfolioUrlList().value),Gson().toJson(itemList))
    }

    /**************************** CheckDataforAtleastOne *******************************************/
    @Test
    fun checkDataForAtleastOneTest(){
        assertEquals(aff.checkDataForAtLeastOne(),false)
    }

    /**************************** UpdateItem *******************************************/
    @Test
    fun updateTest(){
        val position = 1

        aff.updateList(position,"")

        aff.updateFocus(position,true)
        assertEquals(aff.getUpdateItemIndex().value,position)

        aff.updateListErrorState(position)
        assertEquals(aff.getUpdateItemIndex().value,position)

        aff.updateListSuccess(position)
        assertEquals(aff.getUpdateItemIndex().value,position)
    }

    /**************************** FindTextModel *******************************************/
    @Test
    fun findTextModelTest(){
        val id = 16
        assertEquals(aff.finEditTextModelWithId(id),null)
    }

    /**************************** GetCurrentSocialIDs *******************************************/
    @Test
    fun getCurrentSocialIds(){
        assertEquals(aff.getCurrentSocialIds().size,0)
    }
}