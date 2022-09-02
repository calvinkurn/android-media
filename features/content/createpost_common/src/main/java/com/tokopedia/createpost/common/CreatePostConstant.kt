package com.tokopedia.createpost.common

import com.tokopedia.createpost.common.view.viewmodel.ProductSuggestionItem

/**
 * @author by milhamj on 27/02/19.
 */
const val CREATE_POST_ERROR_MSG = "create_post_error_msg"
const val DRAFT_ID = "draft_id"
const val DRAFT_ID_PARAM = "{$DRAFT_ID}"
const val SHOP_ID_PARAM = "{shop_id}"
const val USER_ID_PARAM = "{user_id}"
const val TYPE_CONTENT = "content"
const val TYPE_CONTENT_SHOP = "content-shop"
const val TYPE_CONTENT_USER = "content-user"
const val TYPE_AFFILIATE = "affiliate"
const val TYPE_EDIT = "edit"
const val TYPE_CREATE_POST = "create_post"
const val TYPE_DRAFT = "draft"
const val TOKEN = "token"
const val DI_GET_PROFILE_HEADER_USER_CASE = "GetProfileHeaderUseCase"

typealias SuggestionItemHandler = (ProductSuggestionItem) -> Unit