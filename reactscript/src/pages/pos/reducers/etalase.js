import { PENDING, FULFILLED, REJECTED } from 'redux-promise-middleware'
import {
  FETCH_ETALASE,
  ON_ETALASE_CHANGE,
} from '../actions/index'



export const etalase = (state = {
    items: [{
      id: '0',
      name: 'Semua Etalase',
      alias: 'semua_etalase'
    }],
    selected: '0'
  }, action) => {
    switch (action.type) {
      case `${FETCH_ETALASE}_${PENDING}`:
        return state
      case `${FETCH_ETALASE}_${FULFILLED}`:
        const data = action.payload.data.list || []
        const etalases = data.map(e => ({
          id: e.etalaseId,
          name: e.etalaseName,
          alias: e.etalaseAlias,
        }))
  
        return {
          ...state,
          items: [{
            id: '0',
            name: 'Semua Etalase',
            alias: 'semua_etalase'
          }, ...etalases],
        }
      case `${FETCH_ETALASE}_${REJECTED}`:
        return state
      case ON_ETALASE_CHANGE:
        return {
          ...state,
          selected: action.payload,
        }
      default:
        return state
    }
  }