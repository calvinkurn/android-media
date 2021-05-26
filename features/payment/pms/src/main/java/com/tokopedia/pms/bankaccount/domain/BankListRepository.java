package com.tokopedia.pms.bankaccount.domain;

import com.tokopedia.pms.bankaccount.data.model.BankListModel;

import java.util.List;

/**
 * Created by zulfikarrahman on 7/5/18.
 */

public interface BankListRepository {
    List<BankListModel> getBankList();
}
