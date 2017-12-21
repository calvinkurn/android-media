import React, { Component } from 'react'
import SuccessPage from './pages/SuccessPage'
import TransferPage from './pages/TransferPage'


export default class App extends Component {
    render() {
        const { template } = this.props.data
        
        if (template === 'transfer'){
          return <TransferPage data={this.props.data}/>
        } else if (template === 'instant') {
          return <SuccessPage data={this.props.data}/>
        }
      }
}