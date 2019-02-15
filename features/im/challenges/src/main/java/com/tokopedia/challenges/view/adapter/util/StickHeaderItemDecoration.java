package com.tokopedia.challenges.view.adapter.util;

import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * @author lalit.singh
 */
public class StickHeaderItemDecoration extends RecyclerView.ItemDecoration {

    private StickyHeaderInterface mListener;
    private OnShowMainHeader onShowMainHeader;

    private boolean isHeaderShowing = false;

    public StickHeaderItemDecoration(/*@NonNull StickyHeaderInterface listener*//*, OnShowMainHeader onShowMainHeader*/) {
       // mListener = listener;
       // this.onShowMainHeader = onShowMainHeader;
    }

   /* @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
        View topChild = parent.getChildAt(0);
        if (topChild == null) {
            hideHeader();
            return;
        }

        int topChildPosition = parent.getChildAdapterPosition(topChild);
        if (topChildPosition == RecyclerView.NO_POSITION) {
            hideHeader();
            return;
        }

        int headerPos = mListener.getHeaderPositionForItem(topChildPosition);
        if (headerPos == 0) {
            hideHeader();
            return;
        }


        showHeader();
    }*/

    private void hideHeader() {
        /*if (isHeaderShowing) {
            onShowMainHeader.onShowHeader(false);
            isHeaderShowing = false;
        }*/
    }

    private void showHeader() {
        /*if (!isHeaderShowing) {
            onShowMainHeader.onShowHeader(true);
            isHeaderShowing = true;
        }*/
    }


    public interface StickyHeaderInterface {

        /**
         * @param itemPosition int. Adapter's position of the item for which to do the search of the position of the header item.
         * @return int. Position of the header item in the adapter.
         */
        int getHeaderPositionForItem(int itemPosition);

        /**
         * @param headerPosition int. Position of the header item in the adapter.
         * @return int. Layout resource id.
         */
        int getHeaderLayout(int headerPosition);

        /**
         * @param itemPosition int.
         * @return true, if item at the specified adapter's position represents a header.
         */
        boolean isHeader(int itemPosition);
    }

    public interface OnShowMainHeader {
        void onShowHeader(boolean show);
    }
}