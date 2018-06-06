package com.tokopedia.digital.tokocash.mapper;

import com.tokopedia.digital.exception.MapperDataException;
import com.tokopedia.digital.tokocash.entity.ResponseCashBack;
import com.tokopedia.digital.tokocash.model.CashBackData;

/**
 * Created by kris on 6/16/17. Tokopedia
 */

public interface ITokoCashMapper {

    CashBackData transformTokoCashCashbackData(
            ResponseCashBack responseCashBack
    ) throws MapperDataException;

}
