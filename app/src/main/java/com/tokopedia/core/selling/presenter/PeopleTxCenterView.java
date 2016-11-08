package com.tokopedia.core.selling.presenter;

import com.tokopedia.core.presenter.BaseView;

/**
 * Created by Toped10 on 7/15/2016.
 */
public interface PeopleTxCenterView extends BaseView{
    int PEOPLE_CONFIRM = 1;
    int PEOPLE_VERIFICATION = 2;
    int PEOPLE_STATUS = 3;
    int PEOPLE_CANCEL = 4;
    int PEOPLE_ACCEPT = 5;
    String SHOP = "shop";

    void initView();

    void initHandlerAndAdapter();

    void setCondition1();

    void setCondition2();

    boolean getVisibleUserHint();

    String getState();

    void loadData();
}
