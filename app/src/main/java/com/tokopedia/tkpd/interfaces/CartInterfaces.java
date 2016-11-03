package com.tokopedia.tkpd.interfaces;

/**
 * Created by Tkpd_Eka on 12/16/2014.
 */
public class CartInterfaces {
    public interface FragmentCartCommunicator {
        public void PassResponse(String response);
        public boolean CloseEditMenu();
    }

    public interface FragmentCartSummaryCommunicator {
        public void passIDToTx(String response);
        public void completeKlikpay(String response);
        public void verifyBRIePayment(String tId);
        void completeDynamicPay(String id);
    }


}
