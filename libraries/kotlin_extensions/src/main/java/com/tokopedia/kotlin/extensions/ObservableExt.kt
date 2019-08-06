package com.tokopedia.kotlin.extensions

import rx.Observable

/**
 * @author by milhamj on 2019-08-06.
 */
fun Observable<*>.subscribeIgnoreError() {
    this.subscribe({
        //no op
    }, {
        //ignore error
    })
}