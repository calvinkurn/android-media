package com.tokopedia.createpost

import com.tokopedia.createpost.view.viewmodel.ProductSuggestionItem

/**
 * @author by milhamj on 27/02/19.
 */
const val CREATE_POST_ERROR_MSG = "create_post_error_msg"
const val DRAFT_ID = "draft_id"
const val DRAFT_ID_PARAM = "{$DRAFT_ID}"
const val USER_ID_PARAM = "{user_id}"
const val TYPE_CONTENT_SHOP = "content-shop"
const val TYPE_AFFILIATE = "affiliate"
const val TOKEN = "token"

internal typealias SuggestionItemHandler = (ProductSuggestionItem) -> Unit