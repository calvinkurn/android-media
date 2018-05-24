package com.tokopedia.tokocash.historytokocash.data.mapper;

import com.tokopedia.tokocash.historytokocash.data.entity.WithdrawSaldoEntity;
import com.tokopedia.tokocash.historytokocash.presentation.model.WithdrawSaldo;

import javax.inject.Inject;

import rx.functions.Func1;

/**
 * Created by nabillasabbaha on 10/20/17.
 */

public class WithdrawSaldoMapper implements Func1<WithdrawSaldoEntity, WithdrawSaldo> {

    @Inject
    public WithdrawSaldoMapper() {
    }

    @Override
    public WithdrawSaldo call(WithdrawSaldoEntity withdrawSaldoEntity) {
        if (withdrawSaldoEntity != null) {
            WithdrawSaldo withdrawSaldo = new WithdrawSaldo();

            withdrawSaldo.setAmount(withdrawSaldoEntity.getAmount());
            withdrawSaldo.setDestEmail(withdrawSaldoEntity.getDest_email());
            withdrawSaldo.setWithdrawalId(withdrawSaldoEntity.getWithdrawal_id());
            return withdrawSaldo;
        }
        throw new RuntimeException("Error!");
    }
}
