import {
    FETCH_SHOP_NAME,
    FETCH_SHOP_ID,
} from '../actions/index'
import { PENDING, FULFILLED, REJECTED } from 'redux-promise-middleware'


export const shop = (state = {
    shopName: '',
    shopId: 0,
  }, action) => {
    switch (action.type) {
      case `${FETCH_SHOP_NAME}_${FULFILLED}`:
        return {
          shopName: action.payload
        }
      case `${FETCH_SHOP_ID}_${FULFILLED}`:
        return {
          ...state,
          shopId: action.payload
        }
      default:
        return state
    }
  }