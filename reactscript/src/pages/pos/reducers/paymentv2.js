import { PENDING, FULFILLED, REJECTED } from 'redux-promise-middleware'
import {
  MAKE_PAYMENT_V2,
} from '../actions/index'



export const paymentV2 = (state = {
    processing_isFetchingParams: false,
    processing_showLoadingPage: false,
    processing_data: [],
    processing_status_msg: ''
  }, action) => {
    switch (action.type) {
      case `${MAKE_PAYMENT_V2}_${PENDING}`:
        return {
          ...state,
          processing_isFetchingParams: true,
          processing_showLoadingPage: true,
          processing_data: [],
          processing_status_msg: 'PROCESSING'
        }
      case `${MAKE_PAYMENT_V2}_${FULFILLED}`:
        return {
          ...state,
          processing_isFetchingParams: false,
          processing_showLoadingPage: false,
          processing_data: action.payload,
          processing_status_msg: 'SUCCESS'
        }
      case `${MAKE_PAYMENT_V2}_${REJECTED}`:
        return {
          ...state,
          processing_isFetchingParams: false,
          processing_showLoadingPage: false,
          processing_data: [],
          processing_status_msg: 'FAILED'
        }
  
      default:
        return state
    }
  }