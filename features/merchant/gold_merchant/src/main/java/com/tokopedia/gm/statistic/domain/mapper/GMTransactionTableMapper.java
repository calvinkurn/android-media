package com.tokopedia.gm.statistic.domain.mapper;

import com.tokopedia.gm.statistic.data.source.cloud.model.table.GetTransactionTable;
import com.tokopedia.gm.statistic.domain.model.transaction.table.Cell;
import com.tokopedia.gm.statistic.domain.model.transaction.table.GetTransactionTableModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.functions.Func1;

/**
 * Created by normansyahputa on 7/18/17.
 */

public class GMTransactionTableMapper implements Func1<GetTransactionTable, GetTransactionTableModel> {
    @Inject
    public GMTransactionTableMapper() {
    }


    @Override
    public GetTransactionTableModel call(GetTransactionTable getTransactionTable) {
        GetTransactionTableModel getTransactionTableModel = new GetTransactionTableModel();

        getTransactionTableModel.setTotalCellCount(getTransactionTable.getTotalCellCount());

        List<Cell> cells = new ArrayList<>();
        for (GetTransactionTable.Cell cell : getTransactionTable.getCells()) {
            Cell ret = new Cell();

            ret.setDeliveredAmt(cell.getDeliveredAmt());
            ret.setDeliveredSum(cell.getDeliveredSum());
            ret.setOrderSum(cell.getOrderSum());
            ret.setProductProductId(cell.getProductProductId());
            ret.setProductProductLink(cell.getProductProductLink());
            ret.setProductProductName(cell.getProductProductName());
            ret.setRejectedAmt(cell.getRejectedAmt());
            ret.setRejectedSum(cell.getRejectedSum());
            ret.setTransSum(cell.getTransSum());

            cells.add(ret);
        }

        getTransactionTableModel.setCells(cells);

        return getTransactionTableModel;
    }
}
