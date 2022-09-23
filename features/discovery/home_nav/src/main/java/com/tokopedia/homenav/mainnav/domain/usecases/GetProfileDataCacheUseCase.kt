package com.tokopedia.homenav.mainnav.domain.usecases

import com.tokopedia.homenav.mainnav.data.mapper.AccountHeaderMapper
import com.tokopedia.homenav.mainnav.data.pojo.membership.MembershipPojo
import com.tokopedia.homenav.mainnav.data.pojo.shop.ShopData
import com.tokopedia.homenav.mainnav.data.pojo.user.UserPojo
import com.tokopedia.homenav.mainnav.domain.model.AffiliateUserDetailData
import com.tokopedia.homenav.mainnav.view.datamodel.account.AccountHeaderDataModel
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.coroutines.UseCase
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.coroutineContext

class GetProfileDataCacheUseCase @Inject constructor(
        private val accountHeaderMapper: AccountHeaderMapper,
        private val getUserInfoUseCase: GetUserInfoUseCase,
        private val getUserMembershipUseCase: GetUserMembershipUseCase,
        private val getShopInfoUseCase: GetShopInfoUseCase,
        private val getAffiliateUserUseCase: GetAffiliateUserUseCase
): UseCase<AccountHeaderDataModel>() {

    override suspend fun executeOnBackground(): AccountHeaderDataModel {
        getUserInfoUseCase.setStrategyCache()
        getUserMembershipUseCase.setStrategyCache()
        getShopInfoUseCase.setStrategyCache()
        getAffiliateUserUseCase.setStrategyCache()
        return withContext(coroutineContext){

            var userInfoData: UserPojo? = null
            var userMembershipData: MembershipPojo? = null
            var shopData: ShopData? = null
            var affiliateData: AffiliateUserDetailData? = null

            val getUserInfoCall = async {
                getUserInfoUseCase.executeOnBackground()
            }
            val getUserMembershipCall = async {
                getUserMembershipUseCase.executeOnBackground()
            }
            val getShopInfoCall = async {
                getShopInfoUseCase.executeOnBackground()
            }
            val getAffiliateData = async {
                getAffiliateUserUseCase.executeOnBackground()
            }
            userInfoData = (getUserInfoCall.await().takeIf { it is Success } as? Success<UserPojo>)?.data
            userMembershipData = (getUserMembershipCall.await().takeIf { it is Success } as? Success<MembershipPojo>)?.data
            shopData = (getShopInfoCall.await().takeIf { it is Success } as? Success<ShopData>)?.data
            affiliateData = (getAffiliateData.await().takeIf { it is Success } as? Success<AffiliateUserDetailData>)?.data

            accountHeaderMapper.mapToHeaderModel(
                    userInfoData,
                    null,
                    null,
                    userMembershipData,
                    shopData?.userShopInfo,
                    shopData?.notifications,
                    affiliateData,
                    true,
                    tokopediaPlusParam = null
            )
        }
    }
}