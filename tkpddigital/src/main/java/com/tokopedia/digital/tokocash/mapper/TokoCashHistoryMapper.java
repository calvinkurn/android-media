package com.tokopedia.digital.tokocash.mapper;

import com.tokopedia.digital.tokocash.entity.ActionHistoryEntity;
import com.tokopedia.digital.tokocash.entity.HeaderHistoryEntity;
import com.tokopedia.digital.tokocash.entity.ItemHistoryEntity;
import com.tokopedia.digital.tokocash.entity.TokoCashHistoryEntity;
import com.tokopedia.digital.tokocash.model.ActionHistory;
import com.tokopedia.digital.tokocash.model.HeaderHistory;
import com.tokopedia.digital.tokocash.model.ItemHistory;
import com.tokopedia.digital.tokocash.model.ParamsActionHistory;
import com.tokopedia.digital.tokocash.model.TokoCashHistoryData;

import java.util.ArrayList;
import java.util.List;

import rx.functions.Func1;

/**
 * Created by nabillasabbaha on 8/28/17.
 */

public class TokoCashHistoryMapper implements Func1<TokoCashHistoryEntity, TokoCashHistoryData> {

    @Override
    public TokoCashHistoryData call(TokoCashHistoryEntity tokoCashHistoryEntity) {
        return mappingData(tokoCashHistoryEntity);
    }

    private TokoCashHistoryData mappingData(TokoCashHistoryEntity tokoCashHistoryEntity) {
        TokoCashHistoryData tokoCashHistoryData = new TokoCashHistoryData();

        if (tokoCashHistoryEntity != null) {
            tokoCashHistoryData.setHeaderHistory(getHeaderHistory(tokoCashHistoryEntity.getHeader()));

            if (tokoCashHistoryEntity.getItems() != null) {
                tokoCashHistoryData.setItemHistoryList(getItemHistory(tokoCashHistoryEntity.getItems()));
            }
            tokoCashHistoryData.setNext_uri(tokoCashHistoryEntity.isNext_uri());
        }
        return tokoCashHistoryData;
    }

    private List<HeaderHistory> getHeaderHistory(List<HeaderHistoryEntity> headerHistoryList) {
        List<HeaderHistory> headerHistories = new ArrayList<>();
        for (int i = 0; i < headerHistoryList.size(); i++) {
            HeaderHistory headerHistory = new HeaderHistory();
            if (headerHistoryList != null) {
                headerHistory.setName(headerHistoryList.get(i).getName());
                headerHistory.setType(headerHistoryList.get(i).getType());
                headerHistory.setSelected(headerHistoryList.get(i).isSelected());
            }
            headerHistories.add(headerHistory);
        }
        return headerHistories;
    }

    private List<ItemHistory> getItemHistory(List<ItemHistoryEntity> itemHistoryEntityList) {
        List<ItemHistory> itemHistories = new ArrayList<>();
        for (int i = 0; i < itemHistoryEntityList.size(); i++) {
            ItemHistory itemHistory = new ItemHistory();
            itemHistory.setTransactionId(itemHistoryEntityList.get(i).getTransaction_id());
            itemHistory.setTransactionDetailId(itemHistoryEntityList.get(i).getTransaction_detail_id());
            itemHistory.setTransactionType(itemHistoryEntityList.get(i).getTransaction_type());
            itemHistory.setTitle(itemHistoryEntityList.get(i).getTitle());
            itemHistory.setUrlImage(itemHistoryEntityList.get(i).getIcon_uri());
            itemHistory.setDescription(itemHistoryEntityList.get(i).getDescription());
            itemHistory.setTransactionInfoId(itemHistoryEntityList.get(i).getTransaction_info_id());
            itemHistory.setTransactionInfoDate(itemHistoryEntityList.get(i).getTransaction_info_date());
            itemHistory.setAmount(itemHistoryEntityList.get(i).getAmount());
            itemHistory.setAmountChanges(itemHistoryEntityList.get(i).getAmount_changes());
            itemHistory.setAmountChangesSymbol(itemHistoryEntityList.get(i).getAmount_changes_symbol());
            itemHistory.setNotes(itemHistoryEntityList.get(i).getNotes());
            itemHistory.setAmountPending(itemHistoryEntityList.get(i).getAmount_pending());

            if (itemHistoryEntityList.get(i).getActions() != null) {
                List<ActionHistory> actionHistoryList = new ArrayList<>();
                for (int j = 0; j < itemHistoryEntityList.get(i).getActions().size(); j++) {
                    ActionHistoryEntity actionHistoryEntity = itemHistoryEntityList
                            .get(i).getActions().get(j);
                    ActionHistory actionHistory = new ActionHistory();
                    actionHistory.setName(actionHistoryEntity.getName());
                    actionHistory.setMethod(actionHistoryEntity.getMethod());
                    actionHistory.setTitle(actionHistoryEntity.getTitle());
                    actionHistory.setUrl(actionHistoryEntity.getUrl());

                    ParamsActionHistory paramAction = new ParamsActionHistory();
                    paramAction.setRefundId(actionHistoryEntity.getParams().getRefundId());
                    paramAction.setRefundType(actionHistoryEntity.getParams().getRefundType());
                    actionHistory.setParams(paramAction);
                    actionHistory.setType(actionHistoryEntity.getType());
                    actionHistoryList.add(actionHistory);
                }
                itemHistory.setActionHistoryList(actionHistoryList);
            }
            itemHistories.add(itemHistory);
        }
        return itemHistories;
    }
}