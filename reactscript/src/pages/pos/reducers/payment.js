import { PENDING, FULFILLED, REJECTED } from 'redux-promise-middleware'
import {
  FETCH_BANK_FUlFILLED,
  FETCH_EMI_FUlFILLED,
  MAKE_PAYMENT_FUlFILLED,
} from '../actions/index'



export const payment = (state = {
    items: [],
    emiList: [],
    ccNum: '',
    mon: '',
    year: '',
    cvv: '',
    cardType: ''
  }, action) => {
    switch (action.type) {
      case `${FETCH_BANK_FUlFILLED}_${PENDING}`:
        return {
          ...state,
          isFetching: true,
        }
      case `${FETCH_BANK_FUlFILLED}_${REJECTED}`:
        return {
          ...state,
          isFetching: false
        }
      case `${FETCH_BANK_FUlFILLED}_${FULFILLED}`:
        return {
          ...state,
          items: action.payload
        }
        break;
      case `${FETCH_EMI_FUlFILLED}_${PENDING}`:
        return {
          ...state,
          isFetching: true,
        }
      case `${FETCH_EMI_FUlFILLED}_${REJECTED}`:
        return {
          ...state,
          isFetching: false
        }
      case `${FETCH_EMI_FUlFILLED}_${FULFILLED}`:
        return {
          ...state,
          emiList: action.payload.data.data.list
        }
        break;
  
      case `${MAKE_PAYMENT_FUlFILLED}_${FULFILLED}`:
        return {
          ...state
        }
        break;
      case `${MAKE_PAYMENT_FUlFILLED}_${PENDING}`:
        return {
          ...state,
          isFetching: true,
        }
      case `${MAKE_PAYMENT_FUlFILLED}_${REJECTED}`:
        return {
          ...state,
          isFetching: false
        }
      case 'BANK_SELECTED':
        const bank_id = action.payload
        const newData = {
          items: state.items.map(i => {
            i.isSelected = false;
            if (i.bank_id === bank_id) {
              i.isSelected = true;
            }
            return i
          })
        }
        return { ...state, ...newData };
        break;
      case 'EMI_SELECTED':
        const emiId = action.payload
        const newEmiData = {
          emiList: state.emiList.map(i => {
            i.isSelected = false;
            if (i.id === emiId) {
              i.isSelected = true;
            }
            return i
          })
        }
        return { ...state, ...newEmiData };
        break;
      case 'SELECT_PAYMENT_OPTIONS':
        switch (action.payload.option) {
          case 'mon':
            return { ...state, mon: action.payload.value }
          case 'year':
            return { ...state, year: action.payload.value }
          case 'ccNum':
            return { ...state, ccNum: action.payload.value }
          case 'cvv':
            return { ...state, cvv: action.payload.value }
        }
      default:
        return state
    }
  }