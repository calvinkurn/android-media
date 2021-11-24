package com.tokopedia.autocompletecomponent.initialstate.domain

import com.tokopedia.authentication.AuthHelper
import com.tokopedia.autocompletecomponent.util.DEFAULT_COUNT
import com.tokopedia.autocompletecomponent.util.DEVICE_ID
import com.tokopedia.autocompletecomponent.util.KEY_COUNT
import com.tokopedia.autocompletecomponent.util.SEARCHBAR
import com.tokopedia.discovery.common.constants.SearchApiConst.Companion.DEFAULT_VALUE_OF_PARAMETER_DEVICE
import com.tokopedia.discovery.common.constants.SearchApiConst.Companion.DEVICE
import com.tokopedia.discovery.common.constants.SearchApiConst.Companion.SOURCE
import com.tokopedia.discovery.common.constants.SearchApiConst.Companion.UNIQUE_ID
import com.tokopedia.discovery.common.constants.SearchApiConst.Companion.USER_ID
import com.tokopedia.discovery.common.constants.SearchApiConst.Companion.USER_WAREHOUSE_ID
import com.tokopedia.usecase.RequestParams
import com.tokopedia.user.session.UserSessionInterface

object InitialStateRequestUtils {

    const val INITIAL_STATE_QUERY = """
        query universe_initial_state(${'$'}params: String!){
          universe_initial_state(param: ${'$'}params) {
            data{
              id
              header
              label_action
              feature_id
              tracking_option
              items{
                id
                template
                image_url
                applink
                url
                title
                subtitle
                icon_title
                icon_subtitle
                label
                label_type
                shortcut_image
                type
                discount_percentage
                original_price
                campaign_code
                component_id
              }
            }
          }
        }
    """

    fun getParams(
        searchParameter: Map<String, String>,
        userSession: UserSessionInterface,
        warehouseId: String,
    ): RequestParams {
        val registrationId = userSession.deviceId
        val userId = userSession.userId
        val uniqueId = getUniqueId(userId, registrationId)

        val params = RequestParams.create()
        params.putAll(searchParameter)

        params.putString(DEVICE, DEFAULT_VALUE_OF_PARAMETER_DEVICE)
        params.putString(SOURCE, SEARCHBAR)
        params.putString(KEY_COUNT, DEFAULT_COUNT)
        params.putString(USER_ID, userId)
        params.putString(UNIQUE_ID, uniqueId)
        params.putString(DEVICE_ID, registrationId)

        if (warehouseId.isNotEmpty())
            params.putString(USER_WAREHOUSE_ID, warehouseId)

        return params
    }

    private fun getUniqueId(userId: String, registrationId: String) =
        if (userId.isNotEmpty()) AuthHelper.getMD5Hash(userId)
        else AuthHelper.getMD5Hash(registrationId)
}