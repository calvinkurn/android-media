import { PENDING, FULFILLED, REJECTED } from 'redux-promise-middleware'
import {
  GET_PAYMENT_RATE,
} from '../actions/index'


export const paymentRate = (state = {
    isFetching: false,
    errorMessage: '',
    CreditCardInterestRate: 0,
    CreditCardFlag: 0,
    CreditCardFee: 0,
    InstallmentInterestRate: 0,
    InstallmentFlag: 0,
    InstallmentFee: 0
  }, action) => {
    switch (action.type) {
      case `${GET_PAYMENT_RATE}_${FULFILLED}`:
        return {
          ...state,
          CreditCardInterestRate: action.payload.data.CreditCardInterestRate,
          CreditCardFlag: action.payload.data.CreditCardFlag,
          CreditCardFee: action.payload.data.CreditCardFee,
          InstallmentInterestRate: action.payload.data.InstallmentInterestRate,
          InstallmentFlag: action.payload.data.InstallmentFlag,
          InstallmentFee: action.payload.data.InstallmentFee
        }
  
      case `${GET_PAYMENT_RATE}_${PENDING}`:
        return {
          ...state
        }
  
      case `${GET_PAYMENT_RATE}_${REJECTED}`:
        return {
          ...state,
          errorMessage: action.payload.error_message
        }
  
      default:
        return state
    }
  }