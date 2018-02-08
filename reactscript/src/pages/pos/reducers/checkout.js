import { PENDING, FULFILLED, REJECTED } from 'redux-promise-middleware'
import {
  PAYMENT_CHECKOUT_TO_NATIVE,
} from '../actions/index'



export const checkout = (state = {
    isFetchingParams: false,
    showLoadingPage: false,
    data: [],
    status_msg: ''
  }, action) => {
    switch (action.type) {
      case `${PAYMENT_CHECKOUT_TO_NATIVE}_${PENDING}`:
        return {
          ...state,
          isFetchingParamsCheckout: true,
          showLoadingPage: true,
          data: [],
          status_msg: 'PROCESSING'
        }
      case `${PAYMENT_CHECKOUT_TO_NATIVE}_${FULFILLED}`:
        console.log(action.payload)
        return {
          ...state,
          isFetchingParamsCheckout: false,
          showLoadingPage: false,
          data: action.payload,
          status_msg: 'SUCCESS'
        }
      case `${PAYMENT_CHECKOUT_TO_NATIVE}_${REJECTED}`:
        return {
          ...state,
          isFetchingParamsCheckout: false,
          showLoadingPage: false,
          data: [],
          status_msg: 'FAILED'
        }
  
      default:
        return state
    }
  }