package com.tokopedia.district_recommendation.view;

import com.tokopedia.district_recommendation.domain.usecase.GetShopAddressUseCase;
import com.tokopedia.user.session.UserSession;

/**
 * Created by Irfan Khoirul on 19/11/18.
 */

public interface FragmentListener {

    void setUserSession(UserSession userSession);

    void setShopAddressUseCase(GetShopAddressUseCase shopAddressUseCase);
}
