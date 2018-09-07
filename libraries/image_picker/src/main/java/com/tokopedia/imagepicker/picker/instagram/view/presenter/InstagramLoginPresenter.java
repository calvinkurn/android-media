package com.tokopedia.imagepicker.picker.instagram.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.imagepicker.picker.instagram.domain.interactor.SaveCookiesInstagramUseCase;

/**
 * Created by zulfikarrahman on 6/8/18.
 */

public class InstagramLoginPresenter extends BaseDaggerPresenter {

    private SaveCookiesInstagramUseCase saveCookiesInstagramUseCase;

    public InstagramLoginPresenter(SaveCookiesInstagramUseCase saveCookiesInstagramUseCase) {
        this.saveCookiesInstagramUseCase = saveCookiesInstagramUseCase;
    }

    public void saveCookies(String cookies) {
        saveCookiesInstagramUseCase.executeSync(SaveCookiesInstagramUseCase.createRequestParams(cookies));
    }
}
