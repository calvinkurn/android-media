import { PENDING, FULFILLED, REJECTED } from 'redux-promise-middleware'
import {
    FETCH_TRANSACTION_HISTORY
} from '../actions/index'



export const historyTransaction = (state = {
    items: [],
    page: 0,
    isFetching: false,
    isSuccess: false
  }, action) => {
    switch (action.type) {
      case `FETCH_TRANSACTION_HISTORY_${PENDING}`:
        return {
          ...state,
          isFetching: true,
          isSuccess: false
        }
  
      case `FETCH_TRANSACTION_HISTORY_${FULFILLED}`:
        return {
          ...state,
          items: [...state.items, ...action.payload.data.data.list],
          page: action.payload.page,
          outlet_name: action.payload.outlet_name,
          shop_name: action.payload.shop_name,
          isFetching: false,
          isSuccess: true
        }
  
      case `FETCH_TRANSACTION_HISTORY_${REJECTED}`:
        return {
          ...state,
          isFetching: false,
          isSuccess: false
        }
  
  
      default:
        return state
    }
  }
  