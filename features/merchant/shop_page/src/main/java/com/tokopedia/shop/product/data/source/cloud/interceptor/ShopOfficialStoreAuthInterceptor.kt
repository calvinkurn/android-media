package com.tokopedia.shop.product.data.source.cloud.interceptor

import android.content.Context
import com.tokopedia.network.NetworkRouter
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlist.common.data.interceptor.MojitoInterceptor

class ShopOfficialStoreAuthInterceptor(context: Context?,
                                       networkRouter: NetworkRouter?,
                                       userSessionInterface: UserSessionInterface?) : MojitoInterceptor(context, networkRouter, userSessionInterface)