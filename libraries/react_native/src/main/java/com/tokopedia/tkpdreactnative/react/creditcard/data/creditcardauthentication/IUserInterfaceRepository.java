package com.tokopedia.tkpdreactnative.react.creditcard.data.creditcardauthentication;


import com.tokopedia.abstraction.common.utils.TKPDMapParam;

import rx.Observable;

/**
 * Created by kris on 10/12/17. Tokopedia
 */

public interface IUserInterfaceRepository {

    Observable<String> getUserInfo(TKPDMapParam<String, String> requestUserInfoParam);

}
