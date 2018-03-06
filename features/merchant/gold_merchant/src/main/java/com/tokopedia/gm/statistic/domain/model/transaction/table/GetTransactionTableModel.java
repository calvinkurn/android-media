package com.tokopedia.gm.statistic.domain.model.transaction.table;


import java.util.List;

/**
 * Created by normansyahputa on 7/18/17.
 */

public class GetTransactionTableModel {
    private List<Cell> cells = null;
    private int totalCellCount;

    public List<Cell> getCells() {
        return cells;
    }

    public void setCells(List<Cell> cells) {
        this.cells = cells;
    }

    public int getTotalCellCount() {
        return totalCellCount;
    }

    public void setTotalCellCount(int totalCellCount) {
        this.totalCellCount = totalCellCount;
    }
}
