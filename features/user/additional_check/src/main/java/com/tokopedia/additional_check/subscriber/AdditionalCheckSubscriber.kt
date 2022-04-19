package com.tokopedia.additional_check.subscriber

/**
 * Created by Yoris Prayogo on 2019-11-08.
 * Copyright (c) 2019 PT. Tokopedia All rights reserved.
 */

interface AdditionalCheckSubscriber {
    fun onUrlGenerated(url: String)
    fun onError(msg: String)
}