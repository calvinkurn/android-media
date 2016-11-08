package com.tokopedia.core.selling.model.shopconfirmationdetail;

import java.util.ArrayList;

/**
 * Created by noiz354 on 5/11/16.
 */
public class ShippingConfirmDetModel {
    public String OrderId;
    public ArrayList<Data> datas;
    public ArrayList<DataHistory> dataHistories;

    public static class Data{
        public String ImageUrlList;
        public String NameList;
        public String PriceList;
        public String TtlOrderList;
        public String TtlPriceList;
        public String MessageList;
        public String ProductId;
        public String ProductUrlList;
        public String ProductIdList;
    }

    public static class DataHistory{
        public String DateList;
        public String ActorList;
        public String StateList;
        public String CommentList;
    }
}
