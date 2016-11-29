package com.tokopedia.sellerapp.home.model.rescenter;

/**
 * Created by normansyahputa on 8/31/16.
 */

public class ResCenterInboxDataPassFactory {
    public static ResCenterInboxDataPass defaultResCenterInboxDataPass(){
        ResCenterInboxDataPass resCenterInboxDataPass = new ResCenterInboxDataPass();
        resCenterInboxDataPass.requestAs = 2;
        resCenterInboxDataPass.requestPage = 1;
        resCenterInboxDataPass.sortType = 0;
        resCenterInboxDataPass.filterStatus = 0;
        resCenterInboxDataPass.readUnreadStatus =0;

        return resCenterInboxDataPass;
    }
}
