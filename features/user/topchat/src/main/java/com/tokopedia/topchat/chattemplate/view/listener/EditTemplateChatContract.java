package com.tokopedia.topchat.chattemplate.view.listener;

import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.topchat.chattemplate.view.viewmodel.EditTemplateViewModel;
import com.tokopedia.topchat.chattemplate.view.viewmodel.EditTemplateViewModel;

/**
 * Created by stevenfredian on 12/22/17.
 */

public class EditTemplateChatContract {

    public interface View extends CustomerView {
        void onResult(EditTemplateViewModel editTemplateViewModel, int index, String s);

        void finish();

        void dropKeyboard();

        void showError(String error);

        void onResult(EditTemplateViewModel editTemplateViewModel, int index);
    }

    public interface Presenter extends CustomerPresenter<View> {

        void deleteTemplate(int index);
    }
}
