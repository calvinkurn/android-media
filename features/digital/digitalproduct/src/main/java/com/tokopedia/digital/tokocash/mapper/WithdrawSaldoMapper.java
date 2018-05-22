package com.tokopedia.digital.tokocash.mapper;

import com.tokopedia.digital.tokocash.entity.WithdrawSaldoEntity;
import com.tokopedia.digital.tokocash.model.WithdrawSaldo;

import rx.functions.Func1;

/**
 * Created by nabillasabbaha on 10/20/17.
 */

public class WithdrawSaldoMapper implements Func1<WithdrawSaldoEntity, WithdrawSaldo> {

    @Override
    public WithdrawSaldo call(WithdrawSaldoEntity withdrawSaldoEntity) {
        if (withdrawSaldoEntity != null) {
            WithdrawSaldo withdrawSaldo = new WithdrawSaldo();

            withdrawSaldo.setAmount(withdrawSaldoEntity.getAmount());
            withdrawSaldo.setDestEmail(withdrawSaldoEntity.getDest_email());
            withdrawSaldo.setWithdrawalId(withdrawSaldoEntity.getWithdrawal_id());
            return withdrawSaldo;
        }
        return null;
    }
}
