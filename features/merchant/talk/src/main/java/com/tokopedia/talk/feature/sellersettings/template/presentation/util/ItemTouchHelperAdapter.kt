/*
 * Copyright (C) 2015 Paul Burke
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.tokopedia.talk.feature.sellersettings.template.presentation.util

/*
 * HOW TO USE
 * adapter = new Adapter(OnStartDragListener);
 * ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(adapter);
 * itemTouchHelper = new ItemTouchHelper(callback);
 * itemTouchHelper.attachToRecyclerView(recyclerView);
 *
 * holder.handleView.setOnTouchListener(
 *   if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
 *     mDragStartListener.onStartDrag(holder);
 *   }
 * return false;
 * });
 *
 * onStartDrag(RecyclerView.ViewHolder viewHolder) {
 *   itemTouchHelper.startDrag(viewHolder);
 * }
 *
 * adapter implements ItemTouchHelperAdapter {
 *
 * boolean onItemMove(int fromPosition, int toPosition){
 *   List<Data> data = getData();
 *   Data dataFrom = list.get(fromPosition);
 *   list.remove(fromPosition);
 *   list.add(toPosition, dataFrom);
 *   notifyItemMoved(fromPosition, toPosition);
 * }
 */
/**
 * Interface to listen for a move or dismissal event from a [ItemTouchHelper.Callback].
 *
 * @author Paul Burke (ipaulpro)
 */
interface ItemTouchHelperAdapter {
    /**
     * Called when an item has been dragged far enough to trigger a move. This is called every time
     * an item is shifted, and **not** at the end of a "drop" event.<br></br>
     * <br></br>
     * Implementations should call [RecyclerView.Adapter.notifyItemMoved] after
     * adjusting the underlying data to reflect this move.
     *
     * @param fromPosition The start position of the moved item.
     * @param toPosition   Then resolved position of the moved item.
     * @return True if the item was moved to the new adapter position.
     *
     * @see RecyclerView.getAdapterPositionFor
     * @see RecyclerView.ViewHolder.getAdapterPosition
     */
    fun onItemMove(fromPosition: Int, toPosition: Int): Boolean

    /**
     * Called when an item has been dismissed by a swipe.<br></br>
     * <br></br>
     * Implementations should call [RecyclerView.Adapter.notifyItemRemoved] after
     * adjusting the underlying data to reflect this removal.
     *
     * @param position The position of the item dismissed.
     *
     * @see RecyclerView.getAdapterPositionFor
     * @see RecyclerView.ViewHolder.getAdapterPosition
     */
    fun onItemDismiss(position: Int)
}