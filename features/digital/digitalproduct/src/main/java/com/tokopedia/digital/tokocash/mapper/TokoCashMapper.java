package com.tokopedia.digital.tokocash.mapper;

import com.tokopedia.digital.exception.MapperDataException;
import com.tokopedia.digital.tokocash.entity.ResponseCashBack;
import com.tokopedia.digital.tokocash.model.CashBackData;

/**
 * Created by kris on 6/16/17. Tokopedia
 */

public class TokoCashMapper implements ITokoCashMapper {
    @Override
    public CashBackData transformTokoCashCashbackData(ResponseCashBack responseCashBack)
            throws MapperDataException {
        try {
            CashBackData cashBackData = new CashBackData();
            cashBackData.setAmount(responseCashBack.getAmount());
            cashBackData.setAmountText(responseCashBack.getAmountText());
            return cashBackData;
        } catch (Exception e) {
            throw new MapperDataException(e.getMessage(), e);
        }
    }
}
