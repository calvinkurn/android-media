package com.tokopedia.manageaddress.di

import android.content.Context
import com.google.gson.Gson
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.localizationchooseaddress.domain.response.SetStateChosenAddressQqlResponse
import com.tokopedia.manageaddress.domain.response.shareaddress.KeroShareAddrRequestResponse
import com.tokopedia.manageaddress.domain.response.shareaddress.KeroShareAddrToUserResponse
import com.tokopedia.manageaddress.domain.response.shareaddress.DeleteShareAddressResponse
import com.tokopedia.manageaddress.domain.response.shareaddress.GetSharedAddressListResponse
import com.tokopedia.manageaddress.domain.response.shareaddress.SaveShareAddressResponse
import com.tokopedia.manageaddress.domain.response.shareaddress.SelectShareAddressResponse
import com.tokopedia.manageaddress.domain.response.shareaddress.ValidateShareAddressAsReceiverResponse
import com.tokopedia.manageaddress.domain.response.shareaddress.ValidateShareAddressAsSenderResponse
import com.tokopedia.manageaddress.test.R
import com.tokopedia.test.application.graphql.GqlMockUtil
import com.tokopedia.test.application.graphql.GqlQueryParser
import com.tokopedia.test.application.util.InstrumentationMockHelper

class FakeGraphqlRepository(private val context: Context, private val gson: Gson) : GraphqlRepository {

    private val deleteShareAddressResponse: DeleteShareAddressResponse by lazy {
        gson.fromJson(InstrumentationMockHelper.getRawString(context, R.raw.kero_delete_share_address), DeleteShareAddressResponse::class.java)
    }

    private val saveShareAddressResponse: SaveShareAddressResponse by lazy {
        gson.fromJson(InstrumentationMockHelper.getRawString(context, R.raw.kero_save_share_address), SaveShareAddressResponse::class.java)
    }

    private val selectAddressForShareResponse: SelectShareAddressResponse by lazy {
        gson.fromJson(InstrumentationMockHelper.getRawString(context, R.raw.kero_select_address_for_share), SelectShareAddressResponse::class.java)
    }

    private val sendShareAddressReqResponse: KeroShareAddrRequestResponse by lazy {
        gson.fromJson(InstrumentationMockHelper.getRawString(context, R.raw.kero_send_share_address_request), KeroShareAddrRequestResponse::class.java)
    }

    private val shareAddressToUserResponse: KeroShareAddrToUserResponse by lazy {
        gson.fromJson(InstrumentationMockHelper.getRawString(context, R.raw.kero_share_address_to_user), KeroShareAddrToUserResponse::class.java)
    }

    private val getSharedAddressListResponse: GetSharedAddressListResponse by lazy {
        gson.fromJson(InstrumentationMockHelper.getRawString(context, R.raw.kero_shared_address_list), GetSharedAddressListResponse::class.java)
    }

    private val validateShareAddressAsReceiverResponse: ValidateShareAddressAsReceiverResponse by lazy {
        gson.fromJson(InstrumentationMockHelper.getRawString(context, R.raw.kero_validate_share_address_as_receiver), ValidateShareAddressAsReceiverResponse::class.java)
    }

    private val validateShareAddressAsSenderResponse: ValidateShareAddressAsSenderResponse by lazy {
        gson.fromJson(InstrumentationMockHelper.getRawString(context, R.raw.kero_validate_share_address_as_sender), ValidateShareAddressAsSenderResponse::class.java)
    }

    override suspend fun response(requests: List<GraphqlRequest>, cacheStrategy: GraphqlCacheStrategy): GraphqlResponse {
        return when(GqlQueryParser.parse(requests).first()) {
            KERO_SET_CHOSEN_ADDRES -> GqlMockUtil.createSuccessResponse(SetStateChosenAddressQqlResponse())
            KERO_DELETE_SHARE_ADDRESS -> GqlMockUtil.createSuccessResponse(deleteShareAddressResponse)
            KERO_SAVE_SHARE_ADDRESS -> GqlMockUtil.createSuccessResponse(saveShareAddressResponse)
            KERO_SELECT_ADDRESS_FOR_SHARE -> GqlMockUtil.createSuccessResponse(selectAddressForShareResponse)
            KERO_SEND_SHARE_ADDRESS_REQUEST -> GqlMockUtil.createSuccessResponse(sendShareAddressReqResponse)
            KERO_SHARE_ADDRESS_TO_USER -> GqlMockUtil.createSuccessResponse(shareAddressToUserResponse)
            KERO_GET_SHARED_ADDRESS_LIST -> GqlMockUtil.createSuccessResponse(getSharedAddressListResponse)
            KERO_VALIDATE_SHARE_ADDRESS_AS_RECEIVER -> GqlMockUtil.createSuccessResponse(validateShareAddressAsReceiverResponse)
            KERO_VALIDATE_SHARE_ADDRESS_AS_SENDER -> GqlMockUtil.createSuccessResponse(validateShareAddressAsSenderResponse)
            else -> throw Exception("bad request")
        }
    }

    companion object {
        private const val KERO_SET_CHOSEN_ADDRES = "keroAddrSetStateChosenAddress"
        private const val KERO_DELETE_SHARE_ADDRESS = "KeroAddrDeleteSharedAddress"
        private const val KERO_SAVE_SHARE_ADDRESS = "KeroAddrSaveSharedAddress"
        private const val KERO_SELECT_ADDRESS_FOR_SHARE = "KeroAddrSelectAddressForShareAddressRequest"
        private const val KERO_SEND_SHARE_ADDRESS_REQUEST = "KeroAddrSendShareAddressRequest"
        private const val KERO_SHARE_ADDRESS_TO_USER = "KeroAddrShareAddressToUser"
        private const val KERO_GET_SHARED_ADDRESS_LIST = "KeroAddrGetSharedAddressList"
        private const val KERO_VALIDATE_SHARE_ADDRESS_AS_RECEIVER = "KeroAddrValidateShareAddressRequestAsReceiver"
        private const val KERO_VALIDATE_SHARE_ADDRESS_AS_SENDER = "KeroAddrValidateShareAddressRequestAsSender"
    }
}
