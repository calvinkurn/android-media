package com.tokopedia.pms.bankdestination.domain.interactor;

import com.tokopedia.pms.bankdestination.domain.BankListRepository;
import com.tokopedia.pms.bankdestination.view.model.BankListModel;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by zulfikarrahman on 7/5/18.
 */

public class GetBankListUseCase extends UseCase<List<BankListModel>> {

    private BankListRepository bankListRepository;

    @Inject
    public GetBankListUseCase(BankListRepository bankListRepository) {
        this.bankListRepository = bankListRepository;
    }

    @Override
    public Observable<List<BankListModel>> createObservable(RequestParams requestParams) {
        return bankListRepository.getBankList();
    }
}
