package com.tokopedia.scp_rewards.cabinet.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.scp_rewards.cabinet.analytics.MedalCabinetAnalytics
import com.tokopedia.scp_rewards.cabinet.domain.GetUserMedaliUseCase
import com.tokopedia.scp_rewards.cabinet.domain.MedaliSectionUseCase
import com.tokopedia.scp_rewards.cabinet.domain.model.MedaliCabinetData
import com.tokopedia.scp_rewards.cabinet.domain.model.ScpRewardsGetMedaliSectionResponse
import com.tokopedia.scp_rewards.cabinet.domain.model.getData
import com.tokopedia.scp_rewards.cabinet.mappers.MedaliListMapper
import com.tokopedia.scp_rewards.common.data.Error
import com.tokopedia.scp_rewards.common.data.Loading
import com.tokopedia.scp_rewards.common.data.ScpResult
import com.tokopedia.scp_rewards.common.data.Success
import com.tokopedia.scp_rewards.common.utils.PAGESIZE_PARAM
import com.tokopedia.scp_rewards.common.utils.PAGE_NAME_PARAM
import com.tokopedia.scp_rewards.common.utils.PAGE_PARAM
import com.tokopedia.scp_rewards.common.utils.SOURCE_NAME_PARAM
import com.tokopedia.scp_rewards.common.utils.TYPE_PARAM
import com.tokopedia.scp_rewards.common.utils.launchCatchError
import com.tokopedia.scp_rewards_common.EARNED_BADGE
import com.tokopedia.scp_rewards_common.PROGRESS_BADGE
import com.tokopedia.scp_rewards_common.SUCCESS_CODE
import com.tokopedia.scp_rewards_common.parseJsonKey
import com.tokopedia.usecase.RequestParams
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class MedalCabinetViewModel @Inject constructor(
    private val medaliSectionUseCase: MedaliSectionUseCase,
    private val userMedaliUseCase: GetUserMedaliUseCase,
    private val medalCabinetAnalytics: MedalCabinetAnalytics
) :
    ViewModel() {
    companion object {
        private const val DEFAULT_PAGE_SIZE = 6
        private const val HEADER_SECTION_ID = 1
        private const val EARNED_SECTION_ID = 2
        private const val PROGRESS_SECTION_ID = 3
        private const val PAGE_SIZE_JSON_KEY = "page_size"
        private const val MEDALI_HOME_KEY = "medali_home_page"
    }

    private val _cabinetLiveData: MutableLiveData<ScpResult> = MutableLiveData(Loading)
    val cabinetLiveData: LiveData<ScpResult> = _cabinetLiveData

    private var medaliSlug = ""
    fun getHomePage(medaliSlug: String) {
        this.medaliSlug = medaliSlug
        viewModelScope.launchCatchError(block = {
            val sectionResponse = medaliSectionUseCase.getMedaliHomePageSection(getSectionParams())
            fetchMedalSections(this.coroutineContext, sectionResponse)
        }, onError = {
                _cabinetLiveData.postValue(Error(it))
            })
    }

    private suspend fun fetchMedalSections(coroutineContext: CoroutineContext, sectionResponse: ScpRewardsGetMedaliSectionResponse) {
        withContext(coroutineContext) {
            val earnedPageSize = getPageSize(sectionResponse, EARNED_SECTION_ID)
            val progressPageSize = getPageSize(sectionResponse, PROGRESS_SECTION_ID)

            val earnedMedaliJob = async { userMedaliUseCase.getUserMedalis(getEarnedMedaliParams(pageSize = earnedPageSize)) }
            val progressMedaliJob = async { userMedaliUseCase.getUserMedalis(getProgressMedaliParams(pageSize = progressPageSize)) }

            val earnedMedaliRes = earnedMedaliJob.await()?.getData(SUCCESS_CODE)
            val progressMedaliRes = progressMedaliJob.await()?.getData(SUCCESS_CODE)

            if (earnedMedaliRes == null) {
                medalCabinetAnalytics.sendViewUnlockedMedalSectionApiErrorEvent()
            }
            if (progressMedaliRes == null) {
                medalCabinetAnalytics.sendViewLockedMedalSectionApiErrorEvent()
            }

            MedaliListMapper.apply {
                _cabinetLiveData.postValue(
                    Success(
                        MedaliCabinetData(
                            header = mapSectionResponseToHeaderData(
                                sectionResponse,
                                HEADER_SECTION_ID
                            ),
                            earnedMedaliData = mapMedalTypeResponseToMedalData(
                                sectionResponse,
                                earnedMedaliRes,
                                EARNED_BADGE,
                                EARNED_SECTION_ID
                            ),
                            progressMedaliData = mapMedalTypeResponseToMedalData(
                                sectionResponse,
                                progressMedaliRes,
                                PROGRESS_BADGE,
                                PROGRESS_SECTION_ID
                            )
                        )
                    )
                )
            }
        }
    }

    private fun getPageSize(
        sectionResponse: ScpRewardsGetMedaliSectionResponse,
        sectionId: Int
    ): Int {
        val sectionList = sectionResponse.scpRewardsGetMedaliSectionLayout?.medaliSectionLayoutList
        if (sectionList == null || sectionList.size < 2) {
            return DEFAULT_PAGE_SIZE
        }
        val json = sectionList.find { it.id == sectionId }?.jsonParameter ?: ""
        return json.parseJsonKey<Int>(PAGE_SIZE_JSON_KEY) ?: DEFAULT_PAGE_SIZE
    }

    private fun getSectionParams() = RequestParams().apply {
        putString(SOURCE_NAME_PARAM, MEDALI_HOME_KEY)
        putString(PAGE_NAME_PARAM, MEDALI_HOME_KEY)
    }

    private fun getEarnedMedaliParams(page: Int = 1, pageSize: Int = DEFAULT_PAGE_SIZE) =
        getCommonParams(page, pageSize).apply {
            putString(TYPE_PARAM, EARNED_BADGE)
        }

    private fun getProgressMedaliParams(page: Int = 1, pageSize: Int = DEFAULT_PAGE_SIZE) =
        getCommonParams(page, pageSize).apply {
            putString(TYPE_PARAM, PROGRESS_BADGE)
        }

    private fun getCommonParams(page: Int, pageSize: Int) = RequestParams().apply {
        if (medaliSlug.isNotEmpty()) {
            putString(SOURCE_NAME_PARAM, medaliSlug)
        }
        putString(PAGE_NAME_PARAM, "medali_home_page")
        putInt(PAGE_PARAM, page)
        putInt(PAGESIZE_PARAM, pageSize)
    }
}
